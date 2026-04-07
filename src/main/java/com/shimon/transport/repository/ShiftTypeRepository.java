package com.shimon.transport.repository;

import com.shimon.transport.entity.ShiftType;
import com.shimon.transport.enums.ShiftName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftTypeRepository extends JpaRepository<ShiftType, Long> {

    Optional<ShiftType> findByName(ShiftName name);

    List<ShiftType> findByActiveTrue();
}
