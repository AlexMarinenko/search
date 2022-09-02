package ru.asmsoft.search.model;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * Search result DTO.
 */
@Data
public class SearchResult<T> {

  private final List<T> items;

  private final Metadata metadata;

  /**
   * Search result construction method.
   *
   * @param page page with results
   * @param pager pager object
   * @param <T> type
   * @return SearchResult
   */
  public static <T> SearchResult<T> of(Page<T> page, Pager pager) {
    return new SearchResult<>(
        page.getContent(),
        new Metadata(
            pager.getPage(), pager.getSize(), page.getNumberOfElements(), page.getTotalElements()));
  }

  /**
   * Empty search result.
   * @param pager pager
   * @return empty search result
   * @param <T> type
   */
  public static <T> SearchResult<T> empty(Pager pager) {
    return new SearchResult<>(
        Collections.emptyList(), new Metadata(pager.getPage(), pager.getSize(), 0, 0));
  }

  /**
   * Metadata with pagination information.
   */
  @Data
  public static class Metadata {
    private final int page;
    private final int size;
    private final long count;
    private final long total;
  }
}
