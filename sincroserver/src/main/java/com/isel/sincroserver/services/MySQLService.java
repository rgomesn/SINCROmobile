package com.isel.sincroserver.services;

import com.isel.sincroserver.entities.*;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("MySQLService")
@Transactional
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
        vehicleAuthorization.setAuthorization_end(null);
        int res = repository.insertVehicleAuthorization(vehicleAuthorization);

        VehicleDriver vehicleDriver = new VehicleDriver(0,
                vehicleAuthorization.getCc_number_driver(),
                vehicleAuthorization.getLicence_plate(),
                true,
                Date.from(Instant.now()),
                null);

        return repository.insertVehicleDriver(vehicleDriver) == 1 && res == 1;
    }

    public boolean requestVehicleSubscription(VehicleAuthorization vehicleAuthorization) throws SincroServerException {
        VehicleAuthorization original = repository.getVehicleAuthorization(vehicleAuthorization.getCc_number_owner(),
                vehicleAuthorization.getCc_number_driver(), vehicleAuthorization.getLicence_plate());

        if (original != null && original.getVehicle_authorization_id() > 0) {
            VehicleDriver vehicleDriver = new VehicleDriver(0,
                    original.getCc_number_driver(),
                    original.getLicence_plate(),
                    true,
                    Date.from(Instant.now()),
                    null);

            return repository.insertVehicleDriver(vehicleDriver) == 1;
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

    public List<Vehicle> getVehicles(Citizen citizen) throws SincroServerException {
        return repository.getCitizenVehicles(citizen.getCc_number());
    }

    public List<Vehicle> getOwnedVehicles(Citizen citizen) throws SincroServerException {
        return repository.getCitizenOwnedVehicles(citizen.getCc_number());
    }

    public List<Vehicle> getCitizenDrivenVehicles(Citizen citizen) throws SincroServerException {
        return repository.getCitizenDrivenVehicles(citizen.getCc_number());
    }

    public HashMap<String, List<? extends Infraction>> getPastInfractions(Citizen citizen) throws SincroServerException {
        HashMap<String, List<? extends Infraction>> infractions = new HashMap<>();
        List<Vehicle> citizenVehicles = repository.getCitizenVehicles(citizen.getCc_number());

        for (Vehicle vehicle : citizenVehicles) {
            infractions.put("speedInfractions", repository.getVehicleSpeedInfractions(vehicle.getLicence_plate())
                    .stream()
                    .filter(Infraction::isPaid)
                    .collect(Collectors.toList()));
            infractions.put("redLightInfractions", repository.getVehicleRedLightInfractions(vehicle.getLicence_plate())
                    .stream()
                    .filter(Infraction::isPaid)
                    .collect(Collectors.toList()));
            infractions.put("distanceInfractions", repository.getVehicleDistanceInfractions(vehicle.getLicence_plate())
                    .stream()
                    .filter(Infraction::isPaid)
                    .collect(Collectors.toList()));
        }

        return infractions;
    }

    public boolean insertVehicles(List<Vehicle> vehicles, Citizen citizen) throws SincroServerException {
        for (Vehicle v : vehicles) {
            repository.insertVehicle(v);
            VehicleOwner vo = new VehicleOwner(0, citizen.getCc_number(), v.getLicence_plate(), true, v.getVehicle_date(), null);
            repository.insertVehicleOwner(vo);
        }

        return true;
    }

    public HashMap<String, List<? extends Infraction>> getNewInfractions(Citizen citizen) throws SincroServerException {
        HashMap<String, List<? extends Infraction>> infractions = new HashMap<>();
        List<SpeedInfraction> silist = new ArrayList<>();
        List<RedLightInfraction> rlilist = new ArrayList<>();
        List<DistanceInfraction> dilist = new ArrayList<>();

        List<Notification> notifications = repository.getCitizenToNotifyNotifications(citizen.getCc_number());

        for (Notification n : notifications) {
            Infraction i = repository.getInfraction(n.getInfraction_id());
            SpeedInfraction si= repository.getSpeedInfraction(i.getInfraction_id());

            if (si != null) {
                silist.add(si);
                continue;
            }

            RedLightInfraction rli = repository.getRedLightInfraction(i.getInfraction_id());

            if (rli != null) {
                rlilist.add(rli);
                continue;
            }

            DistanceInfraction di = repository.getDistanceInfraction(i.getInfraction_id());

            if (di != null) {
                dilist.add(di);
                continue;
            }
        }

        for (Notification n : notifications) {
            repository.updateNotification(n.getInfraction_id(), n.getCc_number());
        }

        infractions.put("speedInfractions", silist);
        infractions.put("redLightInfractions", rlilist);
        infractions.put("distanceInfractions", dilist);

        return infractions;
    }

    public boolean insertSpeedInfraction(SpeedInfraction speedInfraction) throws SincroServerException {
        repository.insertInfraction(speedInfraction);

        int res = repository.insertSpeedInfraction(speedInfraction);

        return generateNotifications(speedInfraction.getLicence_plate(), speedInfraction.getInfraction_id());
    }

    public boolean insertRedLightInfraction(RedLightInfraction redLightInfraction) throws SincroServerException {
        repository.insertInfraction(redLightInfraction);

        int res = repository.insertRedLightInfraction(redLightInfraction);

        return generateNotifications(redLightInfraction.getLicence_plate(), redLightInfraction.getInfraction_id());
    }

    public boolean insertDistanceInfraction(DistanceInfraction distanceInfraction) throws SincroServerException {
        repository.insertInfraction(distanceInfraction);

        int res = repository.insertDistanceInfraction(distanceInfraction);

        return generateNotifications(distanceInfraction.getLicence_plate(), distanceInfraction.getInfraction_id());
    }

    private boolean generateNotifications(String licence_plate, long infraction_id) throws SincroServerException {
        List<Citizen> citizens = repository.getVehicleCitizens(licence_plate);

        for (Citizen c : citizens) {
            Notification n = new Notification(0, infraction_id, c.getCc_number(), false, null);
            repository.insertNotification(n);
        }

        return true;
    }

    public Radar getRadar(long radar_id) throws SincroServerException {
        return repository.getRadar(radar_id);
    }

    public boolean payInfraction(long infraction_id) throws SincroServerException {
        return repository.updateInfractionPayment(infraction_id) == 1;
    }

    public List<Notification> getCitizenNotifications(Citizen citizen) throws SincroServerException {
        return repository.getCitizenNotifications(citizen.getCc_number());
    }

    public boolean updateCitizenEmail(Citizen citizen) throws SincroServerException {
        return repository.updateCitizenEmail(citizen.getCc_number(), citizen.getEmail()) == 1;
    }

    public boolean updateCitizenPhoneNumber(Citizen citizen) throws SincroServerException {
        return repository.updateCitizenPhoneNumber(citizen.getCc_number(), citizen.getPhone_number()) == 1;
    }
}