package com.huangpi.platform.common;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> list, int page, int pageSize, long total) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }

    public static <S, T> PageResponse<T> from(Page<S> page, List<T> content) {
        return new PageResponse<>(content, page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }
}
