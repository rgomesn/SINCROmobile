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

    @PostMapping(value="/getUnpaidInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value="/getPastInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value="/getVehicles", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<Vehicle>>> getVehicles(@RequestBody ObjectNode objectNode) {
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
        HashMap<String, List<Vehicle>> vehicles = new HashMap<>();

        try {
            vehicles.put("ownedVehicles", repository.getCitizenOwnedVehicles(citizen.getCc_number()));
            vehicles.put("drivenVehicles", repository.getCitizenDrivenVehicles(citizen.getCc_number()));
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @PostMapping(value="/getRadars", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<Radar>>> getRadars(@RequestBody ObjectNode objectNode) {
        List<Radar> radars;

        try {
            radars = mySQLService.getRadars();
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HashMap<String, List<Radar>> res = new HashMap<>();
        res.put("radars", radars);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value="/getRadar", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Radar>> getRadar(@RequestBody ObjectNode objectNode) {
        Radar radar;

        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("radar_id", long.class));

            if (map == null) {
                logger.error("getVehicles - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getVehicles - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long radar_id = (long)map.get("radar_id");

        try {
            radar = mySQLService.getRadar(radar_id);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HashMap<String, Radar> res = new HashMap<>();
        res.put("radar", radar);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value="/getRadarInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> getRadarInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
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

    @PostMapping(value="/giveVehicleAuthorization", produces= MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value="/requestVehicleSubscription", produces= MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value="/getNewInfractions", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> getNewInfractions(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getNewInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getNewInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");

        HashMap<String, List<? extends Infraction>> infractions;

        try {
            infractions = mySQLService.getNewInfractions(citizen);
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(infractions, HttpStatus.OK);
    }

    @PostMapping(value="/payInfraction", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> payInfraction(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("infraction_id", long.class),
                    new Pair<>("cardNumber", String.class),
                    new Pair<>("expirationDate", String.class),
                    new Pair<>("cvv", String.class));

            if (map == null) {
                logger.error("payInfraction - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("payInfraction - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long infraction_id = (long) map.get("infraction_id");

        try {
            if (!mySQLService.payInfraction(infraction_id)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/getCitizenNotifications", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<Notification>>> getCitizenNotifications(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("getCitizenNotifications - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getCitizenNotifications - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");

        HashMap<String, List<Notification>> notifications = new HashMap<>();

        try {
            notifications.put("notifications", mySQLService.getCitizenNotifications(citizen));
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping(value="/updateCitizenEmail", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> updateCitizenEmail(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("updateUserEmail - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("updateUserEmail - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen =  (Citizen)map.get("citizen");

        try {
            if (!mySQLService.updateCitizenEmail(citizen)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/updateCitizenPhoneNumber", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<? extends Infraction>>> updateCitizenPhoneNumber(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("updateCitizenPhoneNumber - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("updateCitizenPhoneNumber - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen =  (Citizen)map.get("citizen");

        try {
            if (!mySQLService.updateCitizenPhoneNumber(citizen)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}