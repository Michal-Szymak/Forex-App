package com.infoshareacademy.forex_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VolatilityWithTimeDTO extends VolatilityDTO {

    @JsonProperty(value = "date_time")
    private String dateTimeString;

    public VolatilityWithTimeDTO(String type, BigDecimal value, LocalDateTime dateTime, DateTimeFormatter formatter) {
        super(type, value);
        this.dateTimeString = dateTime.format(formatter);
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

}
