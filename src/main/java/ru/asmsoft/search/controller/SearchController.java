package ru.asmsoft.search.controller;

import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;

public interface SearchController<T> {
    SearchResult<T> search(SearchQuery searchQuery);
}
