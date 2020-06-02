package com.isel.sincroapp.data.entities;

public class Radar {
    long radar_id;
    String latitude;
    String longitude;
    String address;
    int kilometer;
    String direction;

    public Radar() {
    }

    public Radar(long radar_id, String latitude, String longitude, String address, int kilometer, String direction) {
        this.radar_id = radar_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.kilometer = kilometer;
        this.direction = direction;
    }

    public long getRadar_id() {
        return radar_id;
    }

    public void setRadar_id(long radar_id) {
        this.radar_id = radar_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getKilometer() {
        return kilometer;
    }

    public void setKilometer(int kilometer) {
        this.kilometer = kilometer;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}