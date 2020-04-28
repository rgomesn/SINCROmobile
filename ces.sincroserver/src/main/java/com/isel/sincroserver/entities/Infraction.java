package com.isel.sincroserver.entities;

import java.util.Date;

public class Infraction {
    long infraction_id;
    long vehicle_driver;
    long radar_id;
    Date infraction_timestamp;
    double price;
    boolean paid;
    Date last_updated;

    public long getInfraction_id() {
        return infraction_id;
    }

    public void setInfraction_id(long infraction_id) {
        this.infraction_id = infraction_id;
    }

    public long getVehicle_driver() {
        return vehicle_driver;
    }

    public void setVehicle_driver(long vehicle_driver) {
        this.vehicle_driver = vehicle_driver;
    }

    public long getRadar_id() {
        return radar_id;
    }

    public void setRadar_id(long radar_id) {
        this.radar_id = radar_id;
    }

    public Date getInfraction_timestamp() {
        return infraction_timestamp;
    }

    public void setInfraction_timestamp(Date infraction_timestamp) {
        this.infraction_timestamp = infraction_timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}