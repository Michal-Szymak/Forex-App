package com.infoshareacademy.forex_app.dto;

import java.math.BigDecimal;

public class ForexEntryDTO {
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volatility;

    public ForexEntryDTO(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volatility = this.high.subtract(this.low);
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }
}
