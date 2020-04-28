package com.isel.sincroserver.entities;

public class Radar {
    long radar_id;
    String latitude;
    String longitude;

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
}