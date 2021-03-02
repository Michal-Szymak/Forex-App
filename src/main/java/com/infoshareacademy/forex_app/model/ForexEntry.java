package com.infoshareacademy.forex_app.model;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Configuration
@Entity
@Table (name = "forex_entries")
@NoArgsConstructor
public class ForexEntry {

    private static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Type(type="uuid-char")
    private UUID id;

    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    @Column(precision=10, scale=6)
    private BigDecimal open;
    @Column(precision=10, scale=6)
    private BigDecimal high;
    @Column(precision=10, scale=6)
    private BigDecimal low;
    @Column(precision=10, scale=6)
    private BigDecimal close;

    public ForexEntry(LocalDate date, LocalTime time, List<BigDecimal> numbers) {
        this.date = date;
        this.time = time;
        this.dateTime = LocalDateTime.of (date, time);
        this.open = numbers.get(0);
        this.high = numbers.get(1);
        this.low = numbers.get(2);
        this.close = numbers.get(3);
    }

    @Override
    public String toString() {
        return "## dateTime=" + dateTime +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                " ##";
    }

    public int getHour() { return time.getHour(); }

    public int getMinute() { return time.getMinute(); }

    public LocalDateTime getDateTime() { return dateTime; }

    public LocalDate getDate() { return date; }

    public LocalTime getTime() { return time; }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }
}