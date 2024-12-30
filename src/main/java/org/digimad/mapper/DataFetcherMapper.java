package org.digimad.mapper;

import graphql.execution.DataFetcherResult;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DataFetcherMapper {

    public static Mono<DataFetcherResult<Map<String, Object>>> mapData(final Mono<Map<String, Object>> data, final String id) {
        return data.map(e -> {
                return DataFetcherResult.<Map<String, Object>>newResult().data(e).build();
            })
            .switchIfEmpty(Mono.defer(() -> {
                throw new RuntimeException(String.format("Car not found with id %s", id));
            }));
    }
}
