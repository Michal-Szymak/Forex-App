package com.infoshareacademy.forex_app.repository;

import com.infoshareacademy.forex_app.model.ForexEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ForexEntryRepository extends JpaRepository<ForexEntry, UUID> {

    List<ForexEntry> findAllByDate(LocalDate date);
    List<ForexEntry> findAllByDateIsBetween(LocalDate first, LocalDate last);
    List<ForexEntry> findAllByDateTime(LocalDateTime dateTime);
    List<ForexEntry> findAllByDateTimeBetween(LocalDateTime first, LocalDateTime last);

}
