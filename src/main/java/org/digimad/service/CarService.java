package org.digimad.service;

import graphql.schema.DataFetchingEnvironment;
import org.digimad.builder.QueryBuilder;
import org.digimad.entity.Car;
import org.digimad.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CarService {

    CarRepository carRepository;
    NamedParameterJdbcTemplate jdbcTemplate;
    QueryBuilder queryBuilder;

    @Autowired
    public CarService(CarRepository carRepository, NamedParameterJdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        this.carRepository = carRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
    }

    public List<Car> getCar(String numberPlate) {
        return carRepository.getOneByNumberPlate(numberPlate);
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Mono<Car> getCarById(Long id) {
        return carRepository.findById(id).map(Mono::just).get();
    }

    public Map<String, Object> getCarByName(String name, DataFetchingEnvironment environment) {
//        String sql = "SELECT CAR_NAME as NAME FROM T_CAR WHERE CAR_NAME=:name";
        String sql = queryBuilder.buildDynamicQuery("car", environment.getSelectionSet().getFields(), Set.of("name"));
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
//        List<Car> carList = jdbcTemplate.query(sql, param, (resultSet, rowNum) -> toPerson(resultSet));
//        List<Map<String, Object>> carList = jdbcTemplate.queryForList(sql, param);
        Map<String, Object> mapObject = jdbcTemplate.queryForMap(sql, param);
//      return Mono.just(mapObject);
        return mapObject;
    }

    private Car toPerson(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setCarId(resultSet.getInt("CAR_ID"));
        car.setName(resultSet.getString("CAR_NAME"));
        return car;
    }
}
