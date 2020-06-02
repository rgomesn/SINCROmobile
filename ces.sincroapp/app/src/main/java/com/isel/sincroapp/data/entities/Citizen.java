package com.isel.sincroapp.data.entities;

import java.util.Objects;

public class Citizen {
    long citizen_id;
    String first_name;
    String middle_name;
    String last_name;
    String cc_number;
    String driver_licence_number;
    String phone_number;
    String email;

    public Citizen() {}

    public Citizen(long citizen_id,
                   String first_name,
                   String middle_name,
                   String last_name,
                   String cc_number,
                   String driver_licence_number,
                   String phone_number,
                   String email) {
        this.citizen_id = citizen_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.cc_number = cc_number;
        this.driver_licence_number = driver_licence_number;
        this.phone_number = phone_number;
        this.email = email;
    }

    public long getCitizen_id() {
        return citizen_id;
    }

    public void setCitizen_id(long citizen_id) {
        this.citizen_id = citizen_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }

    public String getDriver_licence_number() {
        return driver_licence_number;
    }

    public void setDriver_licence_number(String driver_licence_number) {
        this.driver_licence_number = driver_licence_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citizen citizen = (Citizen) o;
        return citizen_id == citizen.citizen_id &&
                Objects.equals(first_name, citizen.first_name) &&
                Objects.equals(middle_name, citizen.middle_name) &&
                Objects.equals(last_name, citizen.last_name) &&
                Objects.equals(cc_number, citizen.cc_number) &&
                Objects.equals(driver_licence_number, citizen.driver_licence_number) &&
                Objects.equals(phone_number, citizen.phone_number) &&
                Objects.equals(email, citizen.email);
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "citizen_id=" + citizen_id +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", cc_number='" + cc_number + '\'' +
                ", driver_licence_number='" + driver_licence_number + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}