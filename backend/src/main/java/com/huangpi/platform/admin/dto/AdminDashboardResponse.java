package com.huangpi.platform.admin.dto;

public record AdminDashboardResponse(long pending, long approvedToday, long merchants, long publishedProducts) {
}
