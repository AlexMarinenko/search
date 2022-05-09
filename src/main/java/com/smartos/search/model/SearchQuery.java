package com.smartos.search.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchQuery {
    private List<SearchCondition> conditions;
    private List<SortOrder> sort;
    private Pager pager;
}
