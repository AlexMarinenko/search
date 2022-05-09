package ru.asmsoft.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pager {
    private int page;
    private int size;
}
