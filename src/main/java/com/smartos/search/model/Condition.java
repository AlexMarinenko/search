package com.smartos.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condition<T extends Comparable<T>> {
    private String field;
    private String operator;
    private Logic logic;
    private T value;
    private List<T> values;
}
