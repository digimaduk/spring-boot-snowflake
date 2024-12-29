package org.digimad.controller;

import org.digimad.entity.Car;
import org.digimad.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> findByNumberPlate(@RequestParam("numberPlate") String numberPlate) {
        return carService.getCar(numberPlate);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

}
