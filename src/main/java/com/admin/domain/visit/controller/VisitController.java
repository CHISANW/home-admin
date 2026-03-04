package com.admin.domain.visit.controller;

import com.admin.domain.visit.dto.VisitCountResponse;
import com.admin.domain.visit.service.VisitService;
import com.admin.global.util.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/visit")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public CommonResponse<Void> recordVisit(HttpServletRequest request) {
        String ip = getClientIp(request);
        visitService.recordVisit(ip);
        return CommonResponse.ok(null);
    }

    @GetMapping("/count")
    public CommonResponse<VisitCountResponse> getTodayVisitorCount() {
        long count = visitService.getTodayVisitorCount();
        return CommonResponse.ok(VisitCountResponse.from(count));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
