package com.infoshareacademy.forex_app.controller;

import com.infoshareacademy.forex_app.dto.VolatilityDTO;
import com.infoshareacademy.forex_app.error.NoForexEntriesFoundException;
import com.infoshareacademy.forex_app.service.VolatilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VolatilityController {

    private final VolatilityService volatilityService;

    public VolatilityController(VolatilityService volatilityService) {
        this.volatilityService = volatilityService;
    }

    @GetMapping("/average_daily_volatility")
    public VolatilityDTO getAverageDailyVolatility() throws NoForexEntriesFoundException {
        return volatilityService.getAverageDailyVolatility();
    }

    @GetMapping("/average_hourly_volatility")
    public VolatilityDTO getAverageHourlyVolatility() throws NoForexEntriesFoundException {
        return volatilityService.getAverageHourlyVolatility();
    }

    @GetMapping("/average_minutely_volatility")
    public VolatilityDTO getAverageMinutelyVolatility() {
        return volatilityService.getAverageMinutelyVolatility();
    }

    @GetMapping("/most_volatile_day")
    public VolatilityDTO getMostVolatileDay() throws NoForexEntriesFoundException {
        return volatilityService.getMostVolatileDay();
    }

    @GetMapping("/most_volatile_hour")
    public VolatilityDTO getMostVolatileHour() throws NoForexEntriesFoundException {
        return volatilityService.getMostVolatileHour();
    }

}
