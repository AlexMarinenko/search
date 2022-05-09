package ru.asmsoft.search.controller;

import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;

/**
 * Search controller interface
 * @param <T> is related Entity type
 */
public interface SearchController<T> {
    SearchResult<T> search(SearchQuery searchQuery);
}
