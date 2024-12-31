package org.digimad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "T_CAR_ACCIDENTS")
public class Accident {

    @Id
    @Column(name = "ACCIDENT_ID")
    private Integer accidentId;

    @Column(name = "CAR_ID")
    private Integer carId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DAMAGE_COST")
    private Double damageCost;

    @Column(name = "ACCIDENT_DT")
    private Date accidentDt;

    public Integer getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(Integer accidentId) {
        this.accidentId = accidentId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(Double damageCost) {
        this.damageCost = damageCost;
    }

    public Date getAccidentDt() {
        return accidentDt;
    }

    public void setAccidentDt(Date accidentDt) {
        this.accidentDt = accidentDt;
    }
}
