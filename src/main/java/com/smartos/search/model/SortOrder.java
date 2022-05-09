package com.smartos.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortOrder {
    private String field;
    private Direction direction;
}
