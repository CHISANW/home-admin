package com.admin.domain.visit.service;

import com.admin.domain.visit.entity.VisitorLog;
import com.admin.domain.visit.repository.VisitorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final VisitorLogRepository visitorLogRepository;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public void recordVisit(String ip) {
        LocalDate today = LocalDate.now(KST);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        if (visitorLogRepository.findByIpAndCreatedAtBetween(ip, start, end).isEmpty()) {
            VisitorLog log = VisitorLog.builder()
                    .ip(ip)
                    .build();
            visitorLogRepository.save(log);
        }
    }

    public long getTodayVisitorCount() {
        LocalDate today = LocalDate.now(KST);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return visitorLogRepository.countByCreatedAtBetween(start, end);
    }
}
