package com.smartos.search.controller;

import com.smartos.search.model.SearchQuery;
import com.smartos.search.model.SearchResult;

public interface SearchController<T> {
    SearchResult<T> search(SearchQuery searchQuery);
}
