package com.isel.sincroapp.data.entities;

import java.io.Serializable;
import java.util.Date;

public class RedLightInfraction extends Infraction implements Serializable {
    int vehicle_speed;
    int elapsed_time;
    int yellow_duration;

    public RedLightInfraction() {
        super();
    }

    public RedLightInfraction(long infraction_id,
                              String licence_plate,
                              long radar_id,
                              Date infraction_date_time,
                              double price,
                              boolean paid,
                              Date payment_date_time,
                              int vehicle_speed,
                              int elapsed_time,
                              int yellow_duration) {
        super(infraction_id, licence_plate, radar_id, infraction_date_time, price, paid, payment_date_time);
        this.vehicle_speed = vehicle_speed;
        this.elapsed_time = elapsed_time;
        this.yellow_duration = yellow_duration;
    }

    public int getVehicle_speed() {
        return vehicle_speed;
    }

    public void setVehicle_speed(int vehicle_speed) {
        this.vehicle_speed = vehicle_speed;
    }

    public int getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(int elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public int getYellow_duration() {
        return yellow_duration;
    }

    public void setYellow_duration(int yellow_duration) {
        this.yellow_duration = yellow_duration;
    }
}
