package com.isel.sincroapp.data.entities;

import java.util.Date;

public class Infraction {
    long infraction_id;
    String licence_plate;
    long radar_id;
    Date infraction_date_time;
    double price;
    boolean paid;
    Date payment_date_time;

    public Infraction() {

    }

    public Infraction(long infraction_id,
                      String licence_plate,
                      long radar_id,
                      Date infraction_date_time,
                      double price,
                      boolean paid,
                      Date payment_date_time) {
        this.infraction_id = infraction_id;
        this.licence_plate = licence_plate;
        this.radar_id = radar_id;
        this.infraction_date_time = infraction_date_time;
        this.price = price;
        this.paid = paid;
        this.payment_date_time = payment_date_time;
    }

    public long getInfraction_id() {
        return infraction_id;
    }

    public void setInfraction_id(long infraction_id) {
        this.infraction_id = infraction_id;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public long getRadar_id() {
        return radar_id;
    }

    public void setRadar_id(long radar_id) {
        this.radar_id = radar_id;
    }

    public Date getInfraction_date_time() {
        return infraction_date_time;
    }

    public void setInfraction_date_time(Date infraction_date_time) {
        this.infraction_date_time = infraction_date_time;
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

    public Date getPayment_date_time() {
        return payment_date_time;
    }

    public void setPayment_date_time(Date payment_date_time) {
        this.payment_date_time = payment_date_time;
    }
}