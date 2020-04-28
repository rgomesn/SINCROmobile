package com.isel.sincroserver.entities;

import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;

import java.util.Date;
import java.util.Objects;

public class Vehicle {
    long vehicle_id;
    String licence_plate;
    String make;
    String model;
    Date _date;

    public Vehicle(long vehicle_id, String licence_plate, String make, String model, Date _date) {
        this.vehicle_id = vehicle_id;
        this.licence_plate = licence_plate;
        this.make = make;
        this.model = model;
        this._date = _date;
    }

    public Vehicle getVehicle(Repository repository) throws SincroServerException {
        return repository.getVehicle(this);
    }

    public void insertVehicle(Repository repository) throws SincroServerException {
        repository.insertVehicle(this);
    }

    public void updateVehicle(Repository repository) throws SincroServerException {
        repository.updateVehicle(this);
    }

    public void deleteVehicle(Repository repository) throws SincroServerException {
        repository.deleteVehicle(this);
    }

    public long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicle_id == vehicle.vehicle_id &&
                licence_plate.equals(vehicle.licence_plate) &&
                make.equals(vehicle.make) &&
                model.equals(vehicle.model) &&
                _date.equals(vehicle._date);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicle_id=" + vehicle_id +
                ", licence_plate='" + licence_plate + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", _date=" + _date +
                '}';
    }
}