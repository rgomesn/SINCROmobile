package com.isel.sincroapp.ui.main;

import com.isel.sincroapp.data.entities.Infraction;

import java.util.Date;

public class ListInfraction {
    String licence_plate;
    long radar_id;
    Date infraction_date_time;
    double price;
    Date payment_date_time;
    Infraction infraction;

    public ListInfraction(String licence_plate, long radar_id, Date infraction_date_time, double price, Date payment_date_time, Infraction infraction) {
        this.licence_plate = licence_plate;
        this.radar_id = radar_id;
        this.infraction_date_time = infraction_date_time;
        this.price = price;
        this.payment_date_time = payment_date_time;
        this.infraction = infraction;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public long getRadar_id() {
        return radar_id;
    }

    public Date getInfraction_date_time() {
        return infraction_date_time;
    }

    public double getPrice() {
        return price;
    }

    public Date getPayment_date_time() {
        return payment_date_time;
    }

    public Infraction getInfraction() {
        return infraction;
    }
}
