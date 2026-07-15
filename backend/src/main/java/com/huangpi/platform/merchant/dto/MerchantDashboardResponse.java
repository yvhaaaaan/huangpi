package com.huangpi.platform.merchant.dto;

import java.time.Instant;
import java.util.List;

public record MerchantDashboardResponse(Stats stats, List<Message> messages, List<Todo> todos) {

    public record Stats(long published, long pending, long draft) {
    }

    public record Message(String id, String type, String title, String content, Instant createdAt) {
    }

    public record Todo(String id, String type, String title, String description, String productId, String level) {
    }
}
