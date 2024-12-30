package org.digimad.controller;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.digimad.entity.Car;
import org.digimad.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
@Slf4j
public class CarGraphqlController {

    @Autowired
    private CarService carService;

    @QueryMapping
    public Mono<Car> findCarById(@Argument final Long id, final DataFetchingEnvironment environment) {
        log.info("Get car by id {}", id);
        return carService.getCarById(id);
    }

    @QueryMapping
    public Map<String, Object> findCarByName(@Argument final String name, final DataFetchingEnvironment environment) {
        log.info("Get car by name {}", name);
        return carService.getCarByName(name, environment);
//        return DataFetcherMapper.mapData(carService.getCarByName(name), name);
    }

}
