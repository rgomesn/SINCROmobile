package com.isel.sincroserver.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isel.sincroserver.entities.*;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.external.ExternalCitizenDataProvider;
import com.isel.sincroserver.interfaces.repositories.Repository;
import com.isel.sincroserver.interfaces.resources.Resources;
import com.isel.sincroserver.services.EmailService;
import com.isel.sincroserver.services.MySQLService;
import com.isel.sincroserver.services.RegistrationService;
import com.isel.sincroserver.utils.Utils;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/sincroapp")
@Qualifier("SincroAppController")
public class SincroAppController {
    private static final Logger logger = LogManager.getLogger(SincroAppController.class);
    @Qualifier("FileResources")
    private final Resources resources;
    @Qualifier("DummyExternalCitizenDataProvider")
    private final ExternalCitizenDataProvider externalCitizenDataProvider;
    @Qualifier("RegistrationService")
    private final RegistrationService registrationService;
    @Qualifier("EmailService")
    private final EmailService emailService;
    @Qualifier("MySQLRepository")
    private final Repository repository;
    @Qualifier("MySQLService")
    private final MySQLService mySQLService;
    @Qualifier("Utils")
    private final Utils utils;

    @Autowired
    public SincroAppController(Resources resources,
                               ExternalCitizenDataProvider externalCitizenDataProvider,
                               RegistrationService registrationService,
                               EmailService emailService,
                               Repository repository,
                               MySQLService mySQLService,
                               Utils utils) {
        this.resources = resources;
        this.externalCitizenDataProvider = externalCitizenDataProvider;
        this.registrationService = registrationService;
        this.emailService = emailService;
        this.repository = repository;
        this.mySQLService = mySQLService;
        this.utils = utils;
    }

    @GetMapping(value="/getUnpaidInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Infraction>> getUnpaidInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getUnpaidInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getUnpaidInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        List<Infraction> unpaidInfractions;

        try {
            unpaidInfractions = mySQLService.getUnpaidInfractions(citizen);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<Infraction>>(unpaidInfractions, HttpStatus.OK);
    }

    @PostMapping(value="/getInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> getInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        HashMap<String, List<? extends Infraction>> infractions;

        try {
            infractions = mySQLService.getInfractions(citizen);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(infractions, HttpStatus.OK);
    }

    @GetMapping(value="/getPastInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> getPastInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getPastInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getPastInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        HashMap<String, List<? extends Infraction>> infractions;

        try {
            infractions = mySQLService.getPastInfractions(citizen);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(infractions, HttpStatus.OK);
    }

    @GetMapping(value="/getVehicles", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Vehicle>> getVehicles(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getVehicles - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getVehicles - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        List<Vehicle> vehicles;

        try {
            vehicles = mySQLService.getVehicles(citizen);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @GetMapping(value="/getRadars", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRadars(@RequestBody ObjectNode objectNode) {
        List<Radar> radars;

        try {
            radars = mySQLService.getRadars();
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(radars, HttpStatus.OK);
    }

    @GetMapping(value="/getRadarInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> getRadarInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class),
                    new Pair<>("radar", Radar.class));

            if (map == null) {
                logger.error("getRadarInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getRadarInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        Radar radar = (Radar)map.get("radar");

        HashMap<String, List<? extends Infraction>> radarInfractions;

        try {
            radarInfractions = mySQLService.getRadarInfractions(citizen, radar);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(radarInfractions, HttpStatus.OK);
    }

    @GetMapping(value="/giveVehicleAuthorization", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> giveVehicleAuthorization(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("vehicleAuthorization", VehicleAuthorization.class));

            if (map == null) {
                logger.error("giveVehicleAuthorization - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("giveVehicleAuthorization - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VehicleAuthorization vehicleAuthorization = (VehicleAuthorization)map.get("vehicleAuthorization");

        try {
            if (!mySQLService.giveVehicleAuthorization(vehicleAuthorization)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/requestVehicleSubscription", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requestVehicleSubscription(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("vehicleAuthorization", VehicleAuthorization.class));

            if (map == null) {
                logger.error("requestVehicleSubscription - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("requestVehicleSubscription - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VehicleAuthorization vehicleAuthorization = (VehicleAuthorization)map.get("vehicleAuthorization");

        try {
            if (!mySQLService.requestVehicleSubscription(vehicleAuthorization)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}