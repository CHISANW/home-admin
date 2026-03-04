package com.admin.domain.visit.repository;

import com.admin.domain.visit.entity.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    Optional<VisitorLog> findByIpAndCreatedAtBetween(String ip, LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
