package com.shimon.transport.repository;

import com.shimon.transport.entity.TransportRequest;
import com.shimon.transport.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportRequestRepository extends JpaRepository<TransportRequest, Long> {

    List<TransportRequest> findByTransportEventId(Long transportEventId);

    List<TransportRequest> findByTransportEventIdAndStatusNot(Long transportEventId, RequestStatus excludeStatus);

    List<TransportRequest> findByUserId(Long userId);

    Optional<TransportRequest> findByUserIdAndTransportEventId(Long userId, Long transportEventId);

    boolean existsByUserIdAndTransportEventId(Long userId, Long transportEventId);
}
