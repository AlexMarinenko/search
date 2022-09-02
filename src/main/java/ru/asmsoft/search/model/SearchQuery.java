package ru.asmsoft.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Search query DTO. */
@Data
@NoArgsConstructor
@Schema(description = "Search query")
public class SearchQuery {
  @Schema(description = "The list of conditions to apply as filters for the data")
  private List<SearchCondition> conditions;

  @Schema(description = "The list of result sortings")
  private List<SortOrder> sort;

  @Schema(description = "Pagination settings for the data")
  private Pager pager;
}
