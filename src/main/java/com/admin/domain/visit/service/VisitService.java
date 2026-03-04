package com.admin.domain.visit.service;

import com.admin.domain.visit.entity.VisitorLog;
import com.admin.domain.visit.repository.VisitorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final VisitorLogRepository visitorLogRepository;

    @Transactional
    public void recordVisit(String ip) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        if (visitorLogRepository.findByIpAndCreatedAtBetween(ip, start, end).isEmpty()) {
            VisitorLog log = VisitorLog.builder()
                    .ip(ip)
                    .build();
            visitorLogRepository.save(log);
        }
    }

    public long getTodayVisitorCount() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return visitorLogRepository.countByCreatedAtBetween(start, end);
    }
}
