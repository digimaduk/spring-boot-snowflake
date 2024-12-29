package org.digimad.repository;

import org.digimad.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    public List<Car> getOneByNumberPlate(String numberPlate);
}
