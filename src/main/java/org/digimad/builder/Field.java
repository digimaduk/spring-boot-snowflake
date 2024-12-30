package org.digimad.builder;

import lombok.Data;

import java.util.List;

@Data
public class Field {
    private String property;
    private String column;
    private Boolean isRequired;
    private List<Join> joins;
    private List<Condition> conditions;
}
