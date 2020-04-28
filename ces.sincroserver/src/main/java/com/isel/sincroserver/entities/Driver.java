package com.isel.sincroserver.entities;

import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;

import java.util.List;
import java.util.Objects;

public class Driver {
    long driver_id;
    String first_name;
    String middle_name;
    String last_name;
    String cc_number;
    String driver_licence_number;
    String phone_number;
    String email;

    public Driver(long driver_id, String first_name, String middle_name, String last_name, String cc_number, String driver_licence_number, String phone_number, String email) {
        this.driver_id = driver_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.cc_number = cc_number;
        this.driver_licence_number = driver_licence_number;
        this.phone_number = phone_number;
        this.email = email;
    }

    public Driver getDriver(Repository repository) throws SincroServerException {
        return repository.getDriver(this);
    }

    public int insertDriver(Repository repository) throws SincroServerException {
        return repository.insertDriver(this);
    }

    public int updateDriver(Repository repository) throws SincroServerException {
        return repository.updateDriver(this);
    }

    public int deleteDriver(Repository repository) throws SincroServerException {
        return repository.deleteDriver(this);
    }

    public static List<Driver> getDrivers(Repository repository) throws SincroServerException {
        return repository.getDrivers();
    }

    public long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(long driver_id) {
        this.driver_id = driver_id;
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
        Driver driver = (Driver) o;
        return driver_id == driver.driver_id &&
                Objects.equals(first_name, driver.first_name) &&
                Objects.equals(middle_name, driver.middle_name) &&
                Objects.equals(last_name, driver.last_name) &&
                Objects.equals(cc_number, driver.cc_number) &&
                Objects.equals(driver_licence_number, driver.driver_licence_number) &&
                Objects.equals(phone_number, driver.phone_number) &&
                Objects.equals(email, driver.email);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driver_id=" + driver_id +
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
