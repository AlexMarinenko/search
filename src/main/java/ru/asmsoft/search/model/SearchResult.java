package ru.asmsoft.search.model;

import java.util.Collections;
import java.util.List;
import lombok.Data;

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
   * @param items the items to include
   * @param pager pager object
   * @param total total items count
   * @param <T> type
   * @return SearchResult
   */
  public static <T> SearchResult<T> of(List<T> items, Pager pager, long total) {
    return new SearchResult<>(
        items,
        new Metadata(
            pager.getPage(), pager.getSize(), items.size(), total));
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
