package org.digimad.builder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinAndAlias {
    private String joinType;
    private String alias;
    private String onCondition;
}
