package com.shimon.transport.repository;

import com.shimon.transport.entity.SpecialDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialDateRepository extends JpaRepository<SpecialDate, Long> {

    Optional<SpecialDate> findBySpecialDate(LocalDate date);

    List<SpecialDate> findBySpecialDateBetween(LocalDate from, LocalDate to);

    List<SpecialDate> findByTransportRelevantTrue();
}
