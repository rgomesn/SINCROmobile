package com.isel.sincroapp.data.entities;

import java.util.Date;

public class VehicleDriver {
    long vehicle_driver_id;
    String cc_number;
    String licence_plate;
    boolean _active;
    Date driving_start;
    Date driving_end;

    public VehicleDriver() {
    }

    public VehicleDriver(long vehicle_driver_id, String cc_number, String licence_plate, boolean _active, Date driving_start, Date driving_end) {
        this.vehicle_driver_id = vehicle_driver_id;
        this.cc_number = cc_number;
        this.licence_plate = licence_plate;
        this._active = _active;
        this.driving_start = driving_start;
        this.driving_end = driving_end;
    }

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

    public boolean is_active() {
        return _active;
    }

    public void set_active(boolean _active) {
        this._active = _active;
    }

    public Date getDriving_start() {
        return driving_start;
    }

    public void setDriving_start(Date driving_start) {
        this.driving_start = driving_start;
    }

    public Date getDriving_end() {
        return driving_end;
    }

    public void setDriving_end(Date driving_end) {
        this.driving_end = driving_end;
    }
}