package com.infoshareacademy.forex_app.controller;

import com.infoshareacademy.forex_app.dto.ForexEntryDTO;
import com.infoshareacademy.forex_app.error.InvalidDateTimeInputException;
import com.infoshareacademy.forex_app.error.NoForexEntriesFoundException;
import com.infoshareacademy.forex_app.service.ForexEntryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/EURUSD")
public class ForexEntryController {

    private final ForexEntryService forexEntryService;

    public ForexEntryController(ForexEntryService forexEntryService) {
        this.forexEntryService = forexEntryService;
    }

    @GetMapping(value = "/{year}")
    public ForexEntryDTO getForexDataForYear(
            @PathVariable Integer year) throws NoForexEntriesFoundException {
        return forexEntryService.getDataForYear(year);
    }

    @GetMapping(value = "/{year}/{month}")
    public ForexEntryDTO getForexDataForMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) throws NoForexEntriesFoundException {
        return forexEntryService.getDataForMonth(year, month);
    }

    @GetMapping(value = "/{year}/{month}/{day}")
    public ForexEntryDTO getForexDataForDay(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @PathVariable Integer day) throws NoForexEntriesFoundException, InvalidDateTimeInputException {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            return forexEntryService.getDataForDay(date);
        } catch (DateTimeException e) {
            throw new InvalidDateTimeInputException();
        }
    }

    @GetMapping(value = "/{year}/{month}/{day}/{hour}")
    public ForexEntryDTO getForexDataForDayAndHour(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @PathVariable Integer day,
            @PathVariable Integer hour) throws NoForexEntriesFoundException, InvalidDateTimeInputException {
        try {
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, 0);
            return forexEntryService.getDataForDayAndHour(dateTime);
        } catch (DateTimeException e) {
            throw new InvalidDateTimeInputException();
        }
    }

    @GetMapping(value = "/{year}/{month}/{day}/{hour}/{minute}")
    public ForexEntryDTO getForexDataForDayAndHour(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @PathVariable Integer day,
            @PathVariable Integer hour,
            @PathVariable Integer minute) throws NoForexEntriesFoundException, InvalidDateTimeInputException {
        try {
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
            return forexEntryService.getDataForDayHourAndMinute(dateTime);
        } catch (DateTimeException e) {
            throw new InvalidDateTimeInputException();
        }
    }

}
