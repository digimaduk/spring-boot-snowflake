package org.digimad.builder;

import lombok.Data;

import java.util.List;

@Data
public class Join {
    private String joinTable;
    private String joinColumnName;
    private String joinType;
    private String valueColumnName;
    private String valueTable;
    private Boolean columnInJoinTable;
    private String joinAlias;
    private List<Condition> fieldConditions;
}
