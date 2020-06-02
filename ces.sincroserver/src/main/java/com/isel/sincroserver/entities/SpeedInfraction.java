package com.isel.sincroserver.entities;

import java.util.Date;

public class SpeedInfraction extends Infraction {
    int vehicle_speed;
    int speed_limit;
    char direction;
    int chasing_vehicle_speed;

    public SpeedInfraction() {
        super();
    }

    public SpeedInfraction(long infraction_id,
                           String licence_plate,
                           long radar_id,
                           Date infraction_date_time,
                           double price,
                           boolean paid,
                           Date payment_date_time,
                           int vehicle_speed,
                           int speed_limit,
                           char direction,
                           int chasing_vehicle_speed) {
        super(infraction_id, licence_plate, radar_id, infraction_date_time, price, paid, payment_date_time);
        this.vehicle_speed = vehicle_speed;
        this.speed_limit = speed_limit;
        this.direction = direction;
        this.chasing_vehicle_speed = chasing_vehicle_speed;
    }

    public int getVehicle_speed() {
        return vehicle_speed;
    }

    public void setVehicle_speed(int vehicle_speed) {
        this.vehicle_speed = vehicle_speed;
    }

    public int getSpeed_limit() {
        return speed_limit;
    }

    public void setSpeed_limit(int speed_limit) {
        this.speed_limit = speed_limit;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getChasing_vehicle_speed() {
        return chasing_vehicle_speed;
    }

    public void setChasing_vehicle_speed(int chasing_vehicle_speed) {
        this.chasing_vehicle_speed = chasing_vehicle_speed;
    }
}
