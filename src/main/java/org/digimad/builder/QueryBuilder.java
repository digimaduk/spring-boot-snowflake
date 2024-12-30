package org.digimad.builder;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class QueryBuilder {

    @Autowired
    private Map<String, Schema> schemaMap; //fields-columns mapping

    public String buildDynamicQuery(final String schemaNm, final List<SelectedField> requestedFields, final Set<String> requiredConditionsSet) {
        Schema schema = Optional.ofNullable(schemaMap.get(schemaNm))
            .orElseThrow(() -> new RuntimeException(String.format("Could not find schema:%s", schemaNm)));

        final StringBuilder builder = new StringBuilder(1024);
        Map<String, JoinAndAlias> alias = new LinkedHashMap<>();
        Map<String, String> queryFields = new LinkedHashMap<>();
        Map<String, String> appliedCond = new LinkedHashMap<>();

        String baseTableAlias = "T0";
        alias.put(schema.getTable() + " " + baseTableAlias, JoinAndAlias.builder().alias(baseTableAlias).build());
        schema.getFields().forEach(f -> {
            if (isFieldSelectable(requestedFields, f)) {
                //adding selected fields and alias
                if (null != f.getJoins()) {
                    f.getJoins().forEach(j -> alias.putIfAbsent(j.getJoinTable() + " " + j.getJoinAlias(), joinValueCondition(j, alias)));
                    String aliasKey = f.getJoins()
                        .stream()
                        .filter(Join::getColumnInJoinTable)
                        .map(Join::getJoinTable)
                        .collect(Collectors.joining())
                        + " "
                        + f.getJoins()
                        .stream()
                        .filter(Join::getColumnInJoinTable)
                        .map(Join::getJoinAlias)
                        .collect(Collectors.joining());
                    queryFields.put(f.getProperty(), alias.get(aliasKey).getAlias() + "." + f.getColumn() + " as " + f.getProperty());
                } else {
                    queryFields.put(f.getProperty(), baseTableAlias + "." + f.getColumn() + " as " + f.getProperty());
                }
                //adding field conditions
                if (null != f.getConditions()) {
                    f.getConditions().forEach(c -> appliedCond.putIfAbsent(c.getProperty(), generateCondition(alias, c)));
                }
            }
        });
        //adding global conditions
        if (!CollectionUtils.isEmpty(schema.getConditions())) {
            schema.getConditions().stream().filter(c -> requiredConditionsSet.contains(c.getProperty()))
                .forEach(c -> appliedCond.putIfAbsent(c.getProperty(), generateCondition(alias, c)));
        }
        //building sql query
        builder.append("select ");
        builder.append(String.join(",", queryFields.values()));
        builder.append(" from ");
        builder.append(alias.entrySet().stream()
            .map(a -> Optional.ofNullable(a.getValue().getJoinType()).orElse("") + "" + a.getKey() + " " + Optional.ofNullable(a.getValue()
                .getOnCondition()).orElse("")).collect(Collectors.joining("")));
        if (!appliedCond.isEmpty()) {
            builder.append(" where ");
            builder.append(String.join(" and ", appliedCond.values()));
        }
        return builder.toString();
    }

    private String generateCondition(final Map<String, JoinAndAlias> alias, final Condition condition) {
        StringBuilder sb = new StringBuilder();
        final String aliasNm = Optional.ofNullable(condition.getJoinAlias()).orElse("T0");
        final boolean isValueInSet = Boolean.TRUE.equals(condition.getValueInSet());
        sb.append(alias.get(condition.getTable() + " " + aliasNm).getAlias())
            .append(".")
            .append(condition.getColumn())
            .append(" " + condition.getOperator() + " ")
            .append(isValueInSet ? "(" : "")
            .append(
                Optional.ofNullable(condition.getValue())
                    .map(c -> Boolean.TRUE.equals(condition.getIsStringType()) ? ("'" + condition.getValue() + "'") : condition.getValue())
                    .orElse(":" + condition.getProperty()))
            .append(isValueInSet ? ")" : "");
        return sb.toString();
    }

    private JoinAndAlias joinValueCondition(final Join j, final Map<String, JoinAndAlias> alias) {
        final Map<String, String> map = new LinkedHashMap<>();
        alias.forEach((k, v) -> map.put(k, v.getAlias()));
        StringBuilder onCondition = new StringBuilder();
        onCondition.append(" ON ")
            .append(j.getJoinAlias())
            .append(".")
            .append(j.getJoinColumnName())
            .append("= ")
            .append(map.get(j.getValueTable()))
            .append(".")
            .append(j.getValueColumnName());
        if (!CollectionUtils.isEmpty(j.getFieldConditions())) {
            j.getFieldConditions().forEach(a -> onCondition.append(" AND ")
                .append(j.getJoinAlias())
                .append(".")
                .append(a.getColumn())
                .append(a.getOperator())
                .append(Optional.ofNullable(a.getValue())
                    .map(c -> Boolean.TRUE.equals(a.getIsStringType()) ? ("'" + a.getValue() + "'") : a.getValue())
                    .orElse(":" + a.getProperty()))
            );
        }
        return JoinAndAlias.builder().joinType(j.getJoinType())
            .alias(j.getJoinAlias())
            .onCondition(onCondition.toString())
            .build();
    }

    private boolean isFieldSelectable(final List<SelectedField> fields, final Field field) {
        if (null == fields) {
            return true;
        }
        return fields.stream().anyMatch(e -> e.getQualifiedName().equals(field.getProperty()))
            || Boolean.TRUE.equals(field.getIsRequired());
    }

    public List<String> getFields(String type, DataFetchingEnvironment environment) {
        // Get the requested fields from the selection set
        List<String> requestedFields = environment.getSelectionSet().getFields().stream()
            .map(SelectedField::getName)
            .collect(Collectors.toList());
        return requestedFields;
    }
}
