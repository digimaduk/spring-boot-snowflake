package org.digimad.service;

import org.digimad.entity.Car;
import org.digimad.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getCar(String numberPlate) {
        return carRepository.getOneByNumberPlate(numberPlate);
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }
}
