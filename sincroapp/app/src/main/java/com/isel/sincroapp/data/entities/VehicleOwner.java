package com.isel.sincroapp.data.entities;

import java.util.Date;

public class VehicleOwner {
    long vehicle_owner_id;
    String cc_number;
    String licence_plate;
    boolean _active;
    Date ownership_start;
    Date ownership_end;

    public VehicleOwner() {
    }

    public VehicleOwner(long vehicle_owner_id, String cc_number, String licence_plate, boolean _active, Date ownership_start, Date ownership_end) {
        this.vehicle_owner_id = vehicle_owner_id;
        this.cc_number = cc_number;
        this.licence_plate = licence_plate;
        this._active = _active;
        this.ownership_start = ownership_start;
        this.ownership_end = ownership_end;
    }

    public long getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(long vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
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

    public Date getOwnership_start() {
        return ownership_start;
    }

    public void setOwnership_start(Date ownership_start) {
        this.ownership_start = ownership_start;
    }

    public Date getOwnership_end() {
        return ownership_end;
    }

    public void setOwnership_end(Date ownership_end) {
        this.ownership_end = ownership_end;
    }
}