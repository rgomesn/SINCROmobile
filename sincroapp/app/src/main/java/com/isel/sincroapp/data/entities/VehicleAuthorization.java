package com.isel.sincroapp.data.entities;

import java.util.Date;

public class VehicleAuthorization {
    long vehicle_authorization_id;
    String cc_number_owner;
    String cc_number_driver;
    String licence_plate;
    boolean _active;
    Date authorization_start;
    Date authorization_end;

    public VehicleAuthorization() {
    }

    public VehicleAuthorization(long vehicle_authorization_id,
                                String cc_number_owner,
                                String cc_number_driver,
                                String licence_plate,
                                boolean _active,
                                Date authorization_start,
                                Date authorization_end) {
        this.vehicle_authorization_id = vehicle_authorization_id;
        this.cc_number_owner = cc_number_owner;
        this.cc_number_driver = cc_number_driver;
        this.licence_plate = licence_plate;
        this._active = _active;
        this.authorization_start = authorization_start;
        this.authorization_end = authorization_end;
    }

    public long getVehicle_authorization_id() {
        return vehicle_authorization_id;
    }

    public void setVehicle_authorization_id(long vehicle_authorization_id) {
        this.vehicle_authorization_id = vehicle_authorization_id;
    }

    public String getCc_number_owner() {
        return cc_number_owner;
    }

    public void setCc_number_owner(String cc_number_owner) {
        this.cc_number_owner = cc_number_owner;
    }

    public String getCc_number_driver() {
        return cc_number_driver;
    }

    public void setCc_number_driver(String cc_number_driver) {
        this.cc_number_driver = cc_number_driver;
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

    public Date getAuthorization_start() {
        return authorization_start;
    }

    public void setAuthorization_start(Date authorization_start) {
        this.authorization_start = authorization_start;
    }

    public Date getAuthorization_end() {
        return authorization_end;
    }

    public void setAuthorization_end(Date authorization_end) {
        this.authorization_end = authorization_end;
    }
}
