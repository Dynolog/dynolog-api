package com.github.appointmentsio.api.domain.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
public class Page<T> {

    @Schema(required = true)
    private final Collection<? extends T> content;

    @Schema(example = "5", required = true)
    private final Integer page;

    @Schema(example = "10", required = true)
    private final Integer size;

    @Schema(example = "27", required = true)
    private final Integer totalPages;

    @Schema(example = "2328", required = true)
    private final Long totalElements;

    public Page(Collection<? extends T> content, Pageable pageable, Long totalElements) {
        this.content = content;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.totalPages = (int) Math.ceil((double)totalElements / this.size);
        this.totalElements = totalElements;
    }

    public Page(Collection<? extends T> content, Integer page, Integer size, Integer totalPages, Long totalElements) {
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

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        var content = this.content.stream().map(converter).toList();
        return new Page<U>(content, this.page, this.size, this.totalPages, this.totalElements);
    }

    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return new Page<>(page);
    }

    public static <T> Page<T> empty() {
        return new Page<T>(List.of(), 0, 0, 0, 0L);
    }
}
