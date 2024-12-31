package org.digimad.service;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.digimad.builder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class PolicyService {
    NamedParameterJdbcTemplate jdbcTemplate;
    QueryBuilder queryBuilder;

    @Autowired
    public PolicyService(NamedParameterJdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
    }

    public Map<String, Object> getPolicyByCarId(Long carId, DataFetchingEnvironment environment) {
        String sql = queryBuilder.buildDynamicQuery("car-insurance-policies", environment.getSelectionSet().getFields(), Set.of("carId"));
        log.info("sql ==> {}", sql);
        SqlParameterSource param = new MapSqlParameterSource().addValue("carId", carId);
        Map<String, Object> mapObject = jdbcTemplate.queryForMap(sql, param);
        return mapObject;
    }
}
