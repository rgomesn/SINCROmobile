package com.isel.sincroserver.entities;

public class VehicleDriver {
    long vehicle_driver_id;
    String cc_number;
    String licence_plate;
    boolean vehicle_owner;

    public long getVehicle_driver_id() {
        return vehicle_driver_id;
    }

    public void setVehicle_driver_id(long vehicle_driver_id) {
        this.vehicle_driver_id = vehicle_driver_id;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public boolean isVehicle_owner() {
        return vehicle_owner;
    }

    public void setVehicle_owner(boolean vehicle_owner) {
        this.vehicle_owner = vehicle_owner;
    }
}