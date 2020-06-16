package com.isel.sincroapp.ui.main;

import java.util.Date;

public class ListInfraction {
    String licence_plate;
    long radar_id;
    Date infraction_date_time;
    double price;
    Date payment_date_time;

    public ListInfraction(String licence_plate, long radar_id, Date infraction_date_time, double price, Date payment_date_time) {
        this.licence_plate = licence_plate;
        this.radar_id = radar_id;
        this.infraction_date_time = infraction_date_time;
        this.price = price;
        this.payment_date_time = payment_date_time;
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
}
