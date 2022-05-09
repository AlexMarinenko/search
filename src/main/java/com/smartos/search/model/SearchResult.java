package com.smartos.search.model;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.Collections;
import java.util.List;

@Data
public class SearchResult<T> {

    private final List<T> items;

    private final Metadata metadata;

    public static <T> SearchResult<T> of(Page<T> page, Pager pager) {
        return new SearchResult<>(page.getContent(), new Metadata(pager.getPage(), pager.getSize(), page.getNumberOfElements(), page.getTotalElements()));
    }

    public static <T> SearchResult<T> empty(Pager pager) {
        return new SearchResult<>(Collections.emptyList(), new Metadata(pager.getPage(), pager.getSize(), 0, 0));
    }

    @Data
    public static class Metadata {
        private final int page;
        private final int size;
        private final long count;
        private final long total;
    }

}
