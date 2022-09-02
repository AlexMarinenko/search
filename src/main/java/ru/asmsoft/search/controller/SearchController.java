package ru.asmsoft.search.controller;

import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;

/**
 * Search controller interface.
 *
 * @param <T> is related Entity type
 */
public interface SearchController<T> {

  /**
   * Search method for the query.
   *
   * @param searchQuery the search query
   * @return search results
   */
  SearchResult<T> search(SearchQuery searchQuery);
}
