package org.digimad.builder;

import lombok.Data;

@Data
public class Condition {
    private String table;
    private String property;
    private String column;
    private String operator;
    private Object value;
    private Boolean isStringType;
    private String joinAlias;
    private Boolean valueInSet;
}
