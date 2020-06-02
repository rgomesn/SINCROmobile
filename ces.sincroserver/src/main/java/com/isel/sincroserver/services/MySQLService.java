package com.isel.sincroserver.services;

import com.isel.sincroserver.entities.*;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Qualifier("MySQLService")
public class MySQLService {
    @Qualifier("MySQLRepository")
    private final Repository repository;

    public MySQLService(Repository repository) {
        this.repository = repository;
    }

    public boolean insertCitizen(Citizen citizen) throws SincroServerException {
        return repository.insertCitizen(citizen) == 1;
    }

    public boolean insertUser(User user) throws SincroServerException {
        return repository.insertUser(user) == 1;
    }

    public List<Infraction> getUnpaidInfractions(Citizen citizen) throws SincroServerException {
        List<Infraction> unpaidInfractions = new ArrayList<>();
        List<Vehicle> citizenVehicles = repository.getCitizenVehicles(citizen.getCc_number());

        for (Vehicle vehicle : citizenVehicles) {
            unpaidInfractions.addAll(repository.getVehicleInfractions(vehicle.getLicence_plate()));
        }

        unpaidInfractions.removeIf(Infraction::isPaid);

        return unpaidInfractions;
    }

    public HashMap<String, List<? extends Infraction>> getInfractions(Citizen citizen) throws SincroServerException {
        HashMap<String, List<? extends Infraction>> infractions = new HashMap<>();
        List<Vehicle> citizenVehicles = repository.getCitizenVehicles(citizen.getCc_number());

        for (Vehicle vehicle : citizenVehicles) {
            infractions.put("speedInfractions", repository.getVehicleSpeedInfractions(vehicle.getLicence_plate()));
            infractions.put("redLightInfractions", repository.getVehicleRedLightInfractions(vehicle.getLicence_plate()));
            infractions.put("distanceInfractions", repository.getVehicleDistanceInfractions(vehicle.getLicence_plate()));
        }

        return infractions;
    }

    public List<Radar> getRadars() throws SincroServerException {
        return repository.getRadars();
    }

    public HashMap<String, List<? extends Infraction>> getRadarInfractions(Citizen citizen, Radar radar) throws SincroServerException {
        HashMap<String, List<? extends Infraction>> infractions = new HashMap<>();
        List<Vehicle> citizenVehicles = repository.getCitizenVehicles(citizen.getCc_number());

        for (Vehicle vehicle : citizenVehicles) {
            infractions.put("speedInfractions", repository.getRadarAndVehicleSpeedInfractions( radar.getRadar_id(), vehicle.getLicence_plate()));
            infractions.put("redLightInfractions", repository.getRadarAndVehicleRedLightInfractions(radar.getRadar_id(), vehicle.getLicence_plate()));
            infractions.put("distanceInfractions", repository.getRadarAndVehicleDistanceInfractions(radar.getRadar_id(), vehicle.getLicence_plate()));
        }

        return infractions;
    }

    public boolean giveVehicleAuthorization(VehicleAuthorization vehicleAuthorization) throws SincroServerException {
        return repository.insertVehicleAuthorization(vehicleAuthorization) == 1;
    }

    public boolean requestVehicleSubscription(VehicleAuthorization vehicleAuthorization) throws SincroServerException {
        VehicleAuthorization original = repository.getVehicleAuthorization(vehicleAuthorization.getCc_number_owner(),
                vehicleAuthorization.getCc_number_driver(), vehicleAuthorization.getLicence_plate());

        if (original.getVehicle_authorization_id() > 0) {
            VehicleDriver vehicleDriver = new VehicleDriver(0,
                    original.getCc_number_driver(),
                    original.getLicence_plate(),
                    true,
                    Date.from(Instant.now()),
                    null);

            repository.insertVehicleDriver(vehicleDriver);

            return true;
        }

        return false;
    }

    public Citizen getCitizen(String cc_number) throws SincroServerException {
        return repository.getCitizen(cc_number);
    }

    public User getCitizenUser(String cc_number) throws SincroServerException {
        return repository.getCitizenUser(cc_number);
    }

    public User getUser(String username) throws SincroServerException {
        return repository.getUser(username);
    }
}