package com.github.appointmentsio.api.domain.pagination;

import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class Page<T> {
    private final Collection<T> content;
    private final Integer page;
    private final Integer size;
    private final Integer totalPages;
    private final Long totalElements;

    public Page(Collection<T> content, Integer page, Integer size, Integer totalPages, Long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public Page(org.springframework.data.domain.Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return new Page<>(page);
    }

    public static <T> Page<T> empty() {
        return new Page<T>(List.of(), 0, 0, 0, 0L);
    }
}
