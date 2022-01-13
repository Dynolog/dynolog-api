package com.github.throyer.apontamentos.domain.shared.pagination;

import static java.util.Objects.nonNull;
import static org.springframework.data.domain.PageRequest.of;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pagination {

    private static final int FIRST_PAGE = 0;

    private static final int DEFAULT_SIZE = 10;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 500;

    private int page = FIRST_PAGE;
    private int size = DEFAULT_SIZE;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (nonNull(page) && page >= FIRST_PAGE) {
            this.page = page;
        } else {
            this.page = FIRST_PAGE;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (nonNull(size) && size >= MIN_SIZE && size <= MAX_SIZE) {
            this.size = size;
        } else {
            this.size = DEFAULT_SIZE;
        }
    }

    public void setSize(String string) {

        if (Objects.isNull(string)) {
            this.size = DEFAULT_SIZE;
            return;
        }

        try {
            var size = Integer.parseInt(string);
            if (size >= MIN_SIZE && size <= MAX_SIZE) {
                this.size = size;
            } else {
                this.size = DEFAULT_SIZE;
            }
        }
        catch (NumberFormatException exception) {
            this.size = DEFAULT_SIZE;
        }
    }

    public Pageable build() {
        return of(page, size);
    }

    public Pageable build(Sort sort) {
        return of(page, size, sort);
    }
}