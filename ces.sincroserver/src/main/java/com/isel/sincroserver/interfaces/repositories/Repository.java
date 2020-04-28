package com.isel.sincroserver.interfaces.repositories;

import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.entities.*;

import java.util.List;

public interface Repository {
    int insertDriver(Driver driver) throws SincroServerException;
    int insertVehicle(Vehicle vehicle) throws SincroServerException;
    int associateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException;
    int insertRadar(Radar radar) throws SincroServerException;
    int insertInfraction(Infraction infraction) throws SincroServerException;
    int deleteDriver(Driver driver) throws SincroServerException;
    int deleteVehicle(Vehicle vehicle) throws SincroServerException;
    int dissociateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException;
    int deleteRadar(Radar radar) throws SincroServerException;
    int deleteInfraction(Infraction infraction) throws SincroServerException;
    int updateDriver(Driver driver) throws SincroServerException;
    int updateVehicle(Vehicle vehicle) throws SincroServerException;
    int updateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException;
    int updateRadar(Radar radar) throws SincroServerException;
    int updateInfraction(Infraction infraction) throws SincroServerException;
    Driver getDriver(Driver driver) throws SincroServerException;
    List<Driver> getDrivers() throws SincroServerException;
    List<Vehicle> getDriverVehicles(Driver driver) throws SincroServerException;
    Vehicle getVehicle(Vehicle vehicle) throws SincroServerException;
    List<Vehicle> getVehicles();
    List<Driver> getVehicleDrivers(Vehicle vehicle) throws SincroServerException;
    Radar getRadar(Radar radar) throws SincroServerException;
    List<Radar> getRadars() throws SincroServerException;
    Infraction getInfraction(Infraction infraction) throws SincroServerException;
    List<Infraction> getInfractions() throws SincroServerException;
    List<Infraction> getDriverInfractions(Driver driver) throws SincroServerException;
    List<Infraction> getVehicleInfractions(Vehicle vehicle) throws SincroServerException;
    List<Infraction> getRadarInfractions(Radar radar);
}