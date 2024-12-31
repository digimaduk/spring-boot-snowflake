package org.digimad.service;

import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
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
public class ReportService {
    NamedParameterJdbcTemplate jdbcTemplate;
    QueryBuilder queryBuilder;

    @Autowired
    public ReportService(NamedParameterJdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
    }

    public List<Map<String, Object>> getReportsByAccidentIds(List<Long> accidentIds, BatchLoaderEnvironment env) {
        String sql = queryBuilder.buildDynamicQuery("car-accident-reports", null, Set.of("accidentId"));
        log.info("sql ==> {}", sql);
        SqlParameterSource param = new MapSqlParameterSource().addValue("accidentId", accidentIds);
        return jdbcTemplate.queryForList(sql, param);
    }
}
