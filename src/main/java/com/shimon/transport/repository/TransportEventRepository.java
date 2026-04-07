package com.shimon.transport.repository;

import com.shimon.transport.entity.TransportEvent;
import com.shimon.transport.enums.TransportEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransportEventRepository extends JpaRepository<TransportEvent, Long> {

    List<TransportEvent> findByEventDateOrderByShiftTypeId(LocalDate eventDate);

    List<TransportEvent> findByEventDateBetweenOrderByEventDateAscShiftTypeIdAsc(
            LocalDate from, LocalDate to);

    List<TransportEvent> findByStatusOrderByEventDateAsc(TransportEventStatus status);

    List<TransportEvent> findByAssignedDriverIdOrderByEventDateAsc(Long driverId);

    Optional<TransportEvent> findByEventDateAndShiftTypeId(LocalDate eventDate, Long shiftTypeId);

    boolean existsByEventDateAndShiftTypeId(LocalDate eventDate, Long shiftTypeId);
}
