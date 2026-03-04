package com.admin.domain.visit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VisitCountResponse {
    private long count;

    public static VisitCountResponse from(long count) {
        return VisitCountResponse.builder()
                .count(count)
                .build();
    }
}
