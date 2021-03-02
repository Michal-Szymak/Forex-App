package com.infoshareacademy.forex_app.service;

import com.infoshareacademy.forex_app.config.DataSourceConfig;
import com.infoshareacademy.forex_app.dto.ForexEntryDTO;
import com.infoshareacademy.forex_app.error.NoForexEntriesFoundException;
import com.infoshareacademy.forex_app.model.ForexDataParser;
import com.infoshareacademy.forex_app.model.ForexEntry;
import com.infoshareacademy.forex_app.repository.ForexEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ForexEntryService {

    private final ForexEntryRepository forexEntryRepository;
    private final ForexDataParser forexDataParser;
    private final DataSourceConfig config;

    public ForexEntryService(ForexEntryRepository forexEntryRepository,
                             ForexDataParser forexDataParser,
                             DataSourceConfig config)
            throws IOException {
        this.forexEntryRepository = forexEntryRepository;
        this.forexDataParser = forexDataParser;
        this.config = config;
        saveEntriesToDatabase(Path.of(this.config.getDefaultFilename()));
    }

    @Transactional
    public void saveEntriesToDatabase(Path path) throws IOException {
        List<ForexEntry> entriesFromFile = forexDataParser.loadEntryList(Path.of(
                System.getProperty("user.dir"),
                "data",
                config.getDefaultFilename()));
        forexEntryRepository.saveAll(entriesFromFile);
    }

    @Transactional
    public void updateDatabase(List<ForexEntry> newEntries) {
        forexEntryRepository.deleteAll();
        forexEntryRepository.saveAll(newEntries);
    }

    public boolean checkFileForForexData(String fileName) throws IOException {
        boolean result = false;
        String defaultFileName = config.getDefaultFilename();

        long lastModifiedFromNewSource = Path.of(
                System.getProperty("user.dir"),
                "data",
                fileName)
                .toFile().lastModified();
        long lastModifiedFromCurrentSource = Path.of(
                System.getProperty("user.dir"),
                "data",
                defaultFileName)
                .toFile().lastModified();

        log.debug("CURRENT FILENAME: " + defaultFileName + " last modified: " + lastModifiedFromCurrentSource);
        log.debug("NEW FILENAME: " + fileName + " last modified: " + lastModifiedFromNewSource);

        if (!fileName.equals(defaultFileName) && lastModifiedFromNewSource != lastModifiedFromCurrentSource) {
                result = isContainingForexData(fileName);
        }

        return result;
    }

    private boolean isContainingForexData(String fileName) throws IOException {
        List<ForexEntry> newForexEntries = forexDataParser.loadEntryList(
                Path.of(System.getProperty("user.dir"), "data", fileName));
        if (!newForexEntries.isEmpty()) {
            updateDatabase(newForexEntries);
            setDefaultFilePath(fileName);
            return true;
        }
        return false;
    }

    private void setDefaultFilePath(String newPath) {
        log.info("Current filepath: " + config.getDefaultFilename());
        config.setDefaultFilename(newPath);
        log.info("Changed to: " + config.getDefaultFilename());
    }

    public ForexEntryDTO getDataForYear(int year) throws NoForexEntriesFoundException {
        LocalDate beginning = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<ForexEntry> filteredEntries = forexEntryRepository.findAllByDateIsBetween(beginning, end);
        log.warn("Number of entries for year " + year + " is: " + filteredEntries.size());
        return getDTOFromList(filteredEntries);
    }

    public ForexEntryDTO getDataForMonth(Integer year, Integer month) throws NoForexEntriesFoundException {
        LocalDate beginning = LocalDate.of(year, month, 1);
        int lengthOfMonth = beginning.lengthOfMonth();
        LocalDate end = LocalDate.of(year, month, lengthOfMonth);
        List<ForexEntry> filteredEntries = forexEntryRepository.findAllByDateIsBetween(beginning, end);
        log.warn("Entries for month " + year + "/" + month + " is: " + filteredEntries.size());
        return getDTOFromList(filteredEntries);
    }

    public ForexEntryDTO getDataForDay(LocalDate date) throws NoForexEntriesFoundException {
        List<ForexEntry> filteredEntries = forexEntryRepository.findAllByDate(date);
        log.warn("Number of entries for day " + date + " is: " + filteredEntries.size());
        return getDTOFromList(filteredEntries);
    }

    public ForexEntryDTO getDataForDayAndHour(LocalDateTime dateTime) throws NoForexEntriesFoundException {
        LocalDateTime beginning = dateTime.withMinute(0);
        LocalDateTime end = dateTime.withMinute(59);
        List<ForexEntry> filteredEntries = forexEntryRepository.findAllByDateTimeBetween(beginning, end);
        log.warn("Number of entries for dateTime " + beginning + " is: " + filteredEntries.size());
        return getDTOFromList(filteredEntries);
    }

    public ForexEntryDTO getDataForDayHourAndMinute(LocalDateTime dateTime) throws NoForexEntriesFoundException {
        List<ForexEntry> filteredEntries = forexEntryRepository.findAllByDateTime(dateTime);
        return getDTOFromList(filteredEntries);
    }

    private ForexEntryDTO getDTOFromList(List<ForexEntry> filteredEntries) throws NoForexEntriesFoundException {
        if (filteredEntries.isEmpty()) throw new NoForexEntriesFoundException();
        BigDecimal open = filteredEntries.get(0).getOpen();
        BigDecimal high = getHighValueFromList(filteredEntries);
        BigDecimal low = getLowValueFromList(filteredEntries);
        BigDecimal close = filteredEntries.get(filteredEntries.size() - 1).getClose();
        return new ForexEntryDTO(open, high, low, close);
    }

    private BigDecimal getHighValueFromList(List<ForexEntry> entries) throws NoForexEntriesFoundException {
        return entries.stream()
                .max(Comparator.comparing(ForexEntry::getHigh))
                .orElseThrow(NoForexEntriesFoundException::new)
                .getHigh();
    }

    private BigDecimal getLowValueFromList(List<ForexEntry> entries) throws NoForexEntriesFoundException {
        return entries.stream()
                .min(Comparator.comparing(ForexEntry::getLow))
                .orElseThrow(NoForexEntriesFoundException::new)
                .getLow();
    }

}
