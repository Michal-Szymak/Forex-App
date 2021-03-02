package com.infoshareacademy.forex_app.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ForexDataParser {

    public List<ForexEntry> loadEntryList(Path filepath) throws IOException{
        ArrayList<ForexEntry> forexEntries = new ArrayList<>();

        log.info("Checking " + filepath + " for Forex Entries.");
        if (Files.exists(filepath)) {
            List<String> lines = Files.readAllLines(filepath);
            readEntriesFromLines(forexEntries, lines);
        }
        log.info("Found " + forexEntries.size() + " entries");
        return forexEntries;
    }

    private void readEntriesFromLines(ArrayList<ForexEntry> result, List<String> lines) {
        for (String s : lines) {
            Optional<ForexEntry> entry = entryFromStringLine(s);
            entry.ifPresent(result::add);
        }
    }

    private Optional<ForexEntry> entryFromStringLine(String s) {
        Scanner scanner = new Scanner(s).useDelimiter(",");
        LocalDate date = null;
        LocalTime time = null;
        List<BigDecimal> numbers = new ArrayList<>();

        if (scanner.hasNext(Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}"))) {
            date = getLocalDateFromString(scanner.next());
        } else return Optional.empty();
        if (scanner.hasNext(Pattern.compile("\\d{2}:\\d{2}"))) {
            time = getLocalTimeFromString(scanner.next());
        } else return Optional.empty();
        while (scanner.hasNextBigDecimal()) {
            numbers.add(scanner.nextBigDecimal());
        }
        if (numbers.size() > 4) return Optional.of(new ForexEntry(date, time, numbers));
        else return Optional.empty();
    }

    private LocalDate getLocalDateFromString(String s) {
        String[] date = s.split("\\.");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        return LocalDate.of(year, month, day);
    }

    private LocalTime getLocalTimeFromString(String s) {
        String[] time = s.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        return LocalTime.of(hour, minute);
    }

}