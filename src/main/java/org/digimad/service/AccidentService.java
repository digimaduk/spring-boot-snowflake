package org.digimad.service;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.digimad.builder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AccidentService {
    NamedParameterJdbcTemplate jdbcTemplate;
    QueryBuilder queryBuilder;

    @Autowired
    public AccidentService(NamedParameterJdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
    }

    public List<Map<String, Object>> getAccidentsByCarId(Long carId, DataFetchingEnvironment environment) {
        String sql = queryBuilder.buildDynamicQuery("car-accidents", environment.getSelectionSet().getFields(), Set.of("carId"));
        log.info("sql ==> {}", sql);
        SqlParameterSource param = new MapSqlParameterSource().addValue("carId", carId);
        return jdbcTemplate.queryForList(sql, param);
    }
}
