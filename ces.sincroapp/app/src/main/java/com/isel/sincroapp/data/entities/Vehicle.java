package com.isel.sincroapp.data.entities;

import java.util.Date;

public class Vehicle {
    long vehicle_id;
    String licence_plate;
    String make;
    String model;
    Date vehicle_date;
    char category;

    public Vehicle() {
    }

    public Vehicle(long vehicle_id, String licence_plate, String make, String model, Date vehicle_date, char category) {
        this.vehicle_id = vehicle_id;
        this.licence_plate = licence_plate;
        this.make = make;
        this.model = model;
        this.vehicle_date = vehicle_date;
        this.category = category;
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

    public Date getVehicle_date() {
        return vehicle_date;
    }

    public void setVehicle_date(Date vehicle_date) {
        this.vehicle_date = vehicle_date;
    }

    public char getCategory() {
        return category;
    }

    public void setCategory(char category) {
        this.category = category;
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
                vehicle_date.equals(vehicle.vehicle_date);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicle_id=" + vehicle_id +
                ", licence_plate='" + licence_plate + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", _date=" + vehicle_date +
                '}';
    }
}