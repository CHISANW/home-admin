package com.admin.domain.visit.entity;

import com.admin.global.util.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitorLog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    private int visitCount;

    @Builder
    public VisitorLog(String ip) {
        this.ip = ip;
        this.visitCount = 1;
    }

    public void incrementVisitCount() {
        this.visitCount++;
    }
}
