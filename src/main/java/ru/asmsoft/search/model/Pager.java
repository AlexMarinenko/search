package ru.asmsoft.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The page number, default = 0", example = "0")
    private int page;
    @Schema(description = "The page size, default = 10", example = "100")
    private int size;
}
