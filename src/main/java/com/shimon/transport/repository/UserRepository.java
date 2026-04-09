package com.shimon.transport.repository;

import com.shimon.transport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByCanDriveTrueAndActiveTrue();

    List<User> findByCanRequestTransportTrueAndActiveTrue();

    List<User> findByActiveTrue();

    /** Returns all users sorted alphabetically by full name — used in the admin user list. */
    List<User> findAllByOrderByFullNameAsc();
}
