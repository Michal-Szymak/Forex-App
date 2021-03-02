package com.infoshareacademy.forex_app.dto;

import java.math.BigDecimal;

public class VolatilityDTO {

    private String type;
    private BigDecimal value;

    public VolatilityDTO(String type, BigDecimal value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
