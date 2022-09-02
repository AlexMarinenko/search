package ru.asmsoft.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

/**
 * Sort order model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortOrder {
  @Schema(description = "The field name to sort by")
  private String field;

  @Schema(description = "Sorting direction. ASC for ascending, DESC for descending orders.")
  private Direction direction;
}
