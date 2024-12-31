package org.digimad.controller;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.digimad.entity.Car;
import org.digimad.service.AccidentService;
import org.digimad.service.CarService;
import org.digimad.service.PolicyService;
import org.digimad.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
public class CarGraphqlController {

    @Autowired
    private CarService carService;
    @Autowired
    private PolicyService policyService;
    @Autowired
    private AccidentService accidentService;
    @Autowired
    private ReportService reportService;

    @QueryMapping
    public Mono<Car> findCarById(@Argument final Long id, final DataFetchingEnvironment environment) {
        log.info("Get car by id {}", id);
        return carService.getCarById(id);
    }

    @QueryMapping
    public Map<String, Object> findCarByName(@Argument final String name, final DataFetchingEnvironment environment) {
        log.info("Get car by name {}", name);
        return carService.getCarByName(name, environment);
//        return DataFetcherMapper.mapData(carService.getCarByName(name, environment), name);
    }

    @QueryMapping
    public List<Map<String, Object>> findCarByYear(@Argument final Long year, final DataFetchingEnvironment environment) {
        log.info("Get cars by year {}", year);
        return carService.getCarByYear(year, environment);
    }

    @SchemaMapping(typeName = "Car", field = "policy")
    public Map<String, Object> findPolicy(final Map<String, Object> map, final DataFetchingEnvironment environment) {
        Long carId = (Long) map.get("ID");
        log.info("Get policy by carId {}", carId);
        return policyService.getPolicyByCarId(carId, environment);
    }

    @SchemaMapping(typeName = "Car", field = "accidents")
    public List<Map<String, Object>> findAccidents(final Map<String, Object> map, final DataFetchingEnvironment environment) {
        Long carId = (Long) map.get("ID");
        log.info("Get accidents by carId {}", carId);
        return accidentService.getAccidentsByCarId(carId, environment);
    }

//    @SchemaMapping(typeName = "Accident", field = "report")
//    public Map<String, Object> findReport(final Map<String, Object> mapList, final DataFetchingEnvironment environment) {
//        Long accidentId = (Long) mapList.get("ID");
//        log.info("Get reports by accidentId {}", accidentId);
//        return reportService.getReportsByAccidentId(accidentId, environment);
//    }

    @BatchMapping(typeName = "Accident", field = "report", maxBatchSize = 100)
    public List<Map<String, Object>> findReport(final List<Map<String, Object>> mapList, final BatchLoaderEnvironment env) {
//        var ids = mapList.stream()
//            .flatMap(map -> map.values().stream())
////            .map(Object::toString)
//            .map(value -> (Long) value)
//            .collect(Collectors.toList());
        var ids = mapList.stream()
            .map(map -> map.get("ID"))
            .filter(Objects::nonNull)
            .map(value -> (Long) value).toList();
        log.info("Get reports by accidentIds {}", ids);
        return reportService.getReportsByAccidentIds(ids, env);
    }
}
