package com.infoshareacademy.forex_app.service;

import com.infoshareacademy.forex_app.dto.VolatilityDTO;
import com.infoshareacademy.forex_app.dto.VolatilityWithTimeDTO;
import com.infoshareacademy.forex_app.error.NoForexEntriesFoundException;
import com.infoshareacademy.forex_app.model.ForexEntry;
import com.infoshareacademy.forex_app.repository.ForexEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VolatilityService {

    private static final LocalDate DEFAULT_DATE = LocalDate.of(1900, 1, 1);
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:00");
    private final LocalTime DEFAULT_TIME = LocalTime.of(0, 0);

    private final ForexEntryRepository repository;

    public VolatilityService(ForexEntryRepository repository) {
        this.repository = repository;
    }

    public VolatilityDTO getAverageDailyVolatility() throws NoForexEntriesFoundException {
        BigDecimal value =  calculateAverageVolatility(separateEntriesByDay());
        return new VolatilityDTO("Average daily volatility", value.setScale(8, RoundingMode.HALF_UP));
    }

    public VolatilityDTO getAverageHourlyVolatility() throws NoForexEntriesFoundException {
        BigDecimal value =  calculateAverageVolatility(separateEntriesByDayAndHour());
        return new VolatilityDTO("Average hourly volatility", value.setScale(8, RoundingMode.HALF_UP));
    }

    public VolatilityDTO getAverageMinutelyVolatility() {
        Double average = repository.findAll().stream()
                .collect(Collectors.averagingDouble(x -> x.getHigh().subtract(x.getLow()).doubleValue()));
        BigDecimal value = BigDecimal.valueOf(average).setScale(8, RoundingMode.HALF_UP);
        return new VolatilityDTO("Average minutely volatility", value);
    }

    public VolatilityDTO getMostVolatileDay() throws NoForexEntriesFoundException {
        return getDtoOfMostVolatileTimeframe("Most volatile day", separateEntriesByDay(), DAY_FORMATTER);
    }

    public VolatilityDTO getMostVolatileHour() throws NoForexEntriesFoundException {
        return getDtoOfMostVolatileTimeframe
                ("Most volatile hour", separateEntriesByDayAndHour(), HOUR_FORMATTER);
    }

    private BigDecimal calculateAverageVolatility (List<List<ForexEntry>> lists) throws NoForexEntriesFoundException {
        BigDecimal sum = BigDecimal.ZERO;
        for (List<ForexEntry> entryList : lists) {
            sum = sum.add(getVolatilityFromList(entryList));
        }
        return sum.divide(BigDecimal.valueOf(lists.size()), RoundingMode.HALF_UP);
    }

    private VolatilityDTO getDtoOfMostVolatileTimeframe
            (String type, List<List<ForexEntry>> lists, DateTimeFormatter formatter)
            throws NoForexEntriesFoundException {
        LocalDateTime outputDateTime = LocalDateTime.of(DEFAULT_DATE, DEFAULT_TIME);
        BigDecimal maxVolatility = BigDecimal.ZERO;
        for (List<ForexEntry> entryList : lists) {
            BigDecimal volatility = getVolatilityFromList(entryList);
            if (volatility.compareTo(maxVolatility) > 0) {
                maxVolatility = volatility;
                outputDateTime = entryList.get(0).getDateTime();
                log.info("Changed max volatility to " + maxVolatility + " at " + outputDateTime.format(formatter));
            }
        }
        log.info("Returning max volatility: " + maxVolatility);
        return new VolatilityWithTimeDTO(type, maxVolatility, outputDateTime, formatter);
    }

    private BigDecimal getVolatilityFromList(List<ForexEntry> forexEntries) throws NoForexEntriesFoundException {
        BigDecimal high = forexEntries.stream()
                .max(Comparator.comparing(ForexEntry::getHigh))
                .map(ForexEntry::getHigh)
                .orElseThrow(NoForexEntriesFoundException::new);

        BigDecimal low = forexEntries.stream()
                .min(Comparator.comparing(ForexEntry::getLow))
                .map(ForexEntry::getLow)
                .orElseThrow(NoForexEntriesFoundException::new);

        return high.subtract(low);
    }

    public List<List<ForexEntry>> separateEntriesByDay() {
        List<List<ForexEntry>> result = new ArrayList<>();
        List<LocalDate> dates = repository.findAll().stream()
                .map(ForexEntry::getDate)
                .distinct()
                .collect(Collectors.toList());

        for (LocalDate date : dates) {
            result.add(repository.findAll().stream()
                    .filter(entry -> entry.getDate().equals(date))
                    .collect(Collectors.toList())
            );
        }
        return result;
    }

    public List<List<ForexEntry>> separateEntriesByDayAndHour() {
        List<List<ForexEntry>> result = new ArrayList<>();
        List<LocalDateTime> datesAndHours = repository.findAll().stream()
                .map(ForexEntry::getDateTime)
                .map(x -> x.withMinute(0))
                .distinct()
                .collect(Collectors.toList());

        for (LocalDateTime dateTime : datesAndHours) {
            result.add(repository.findAll().stream()
                    .filter(entry -> entry.getDate().equals(dateTime.toLocalDate()))
                    .filter(entry -> entry.getHour() == dateTime.getHour())
                    .collect(Collectors.toList())
            );
        }
        return result;
    }

}
