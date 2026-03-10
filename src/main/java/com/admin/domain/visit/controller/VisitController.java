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

import java.util.regex.Pattern;

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

    private static final Pattern IPV4_PATTERN =
            Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");

    private String getClientIp(HttpServletRequest request) {
        String ip = extractFirstIp(request.getHeader("X-Forwarded-For"));
        if (!isValidIpv4(ip)) ip = extractFirstIp(request.getHeader("Proxy-Client-IP"));
        if (!isValidIpv4(ip)) ip = extractFirstIp(request.getHeader("WL-Proxy-Client-IP"));
        if (!isValidIpv4(ip)) ip = extractFirstIp(request.getHeader("HTTP_CLIENT_IP"));
        if (!isValidIpv4(ip)) ip = extractFirstIp(request.getHeader("HTTP_X_FORWARDED_FOR"));
        if (!isValidIpv4(ip)) ip = normalizeIp(request.getRemoteAddr());
        return ip;
    }

    private String extractFirstIp(String header) {
        if (header == null || header.isBlank() || "unknown".equalsIgnoreCase(header)) {
            return null;
        }
        // X-Forwarded-For: client, proxy1, proxy2 → 첫 번째 IP만 사용
        String first = header.split(",")[0].trim();
        return normalizeIp(first);
    }

    private String normalizeIp(String ip) {
        if (ip == null) return null;
        // IPv4-mapped IPv6 (::ffff:x.x.x.x) → IPv4로 변환
        if (ip.startsWith("::ffff:") || ip.startsWith("::FFFF:")) {
            return ip.substring(7);
        }
        return ip;
    }

    private boolean isValidIpv4(String ip) {
        return ip != null && IPV4_PATTERN.matcher(ip).matches();
    }
}
