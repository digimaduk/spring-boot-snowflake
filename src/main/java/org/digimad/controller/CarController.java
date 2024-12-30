package org.digimad.controller;

import lombok.extern.slf4j.Slf4j;
import org.digimad.entity.Car;
import org.digimad.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Slf4j
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> findByNumberPlate(@RequestParam("numberPlate") String numberPlate) {
        log.info("Finding Car by Number Plate: {}", numberPlate);
        return carService.getCar(numberPlate);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        log.info("Adding Car");
        return carService.addCar(car);
    }

}
