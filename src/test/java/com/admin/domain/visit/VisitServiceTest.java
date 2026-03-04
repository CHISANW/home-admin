package com.admin.domain.visit;

import com.admin.domain.visit.repository.VisitorLogRepository;
import com.admin.domain.visit.service.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitorLogRepository visitorLogRepository;

    @BeforeEach
    void setUp() {
        visitorLogRepository.deleteAll();
    }

    @Test
    @DisplayName("동일한 IP로 하루에 한 번만 방문 기록이 저장된다")
    void recordVisitOncePerDay() {
        // given
        String ip = "127.0.0.1";

        // when
        visitService.recordVisit(ip);
        visitService.recordVisit(ip);
        visitService.recordVisit(ip);

        // then
        long count = visitorLogRepository.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("다른 IP는 각각 방문 기록이 저장된다")
    void recordVisitDifferentIp() {
        // given
        String ip1 = "127.0.0.1";
        String ip2 = "192.168.0.1";

        // when
        visitService.recordVisit(ip1);
        visitService.recordVisit(ip2);

        // then
        long count = visitorLogRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("오늘의 방문자 수를 조회한다")
    void getTodayVisitorCount() {
        // given
        visitService.recordVisit("127.0.0.1");
        visitService.recordVisit("127.0.0.2");
        visitService.recordVisit("127.0.0.1"); // 중복

        // when
        long count = visitService.getTodayVisitorCount();

        // then
        assertThat(count).isEqualTo(2);
    }
}
