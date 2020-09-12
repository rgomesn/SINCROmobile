package com.isel.sincroapp.data.entities;

import java.io.Serializable;
import java.util.Date;

public class DistanceInfraction extends Infraction implements Serializable {
    int distance_to_next_vehicle;
    int distance_limit;

    public DistanceInfraction() {
        super();
    }

    public DistanceInfraction(long infraction_id,
                              String licence_plate,
                              long radar_id,
                              Date infraction_date_time,
                              double price,
                              boolean paid,
                              Date payment_date_time,
                              int distance_to_next_vehicle,
                              int distance_limit) {
        super(infraction_id, licence_plate, radar_id, infraction_date_time, price, paid, payment_date_time);
        this.distance_to_next_vehicle = distance_to_next_vehicle;
        this.distance_limit = distance_limit;
    }

    public int getDistance_to_next_vehicle() {
        return distance_to_next_vehicle;
    }

    public void setDistance_to_next_vehicle(int distance_to_next_vehicle) {
        this.distance_to_next_vehicle = distance_to_next_vehicle;
    }

    public int getDistance_limit() {
        return distance_limit;
    }

    public void setDistance_limit(int distance_limit) {
        this.distance_limit = distance_limit;
    }
}
