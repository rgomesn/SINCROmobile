package com.isel.sincroserver.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isel.sincroserver.entities.DistanceInfraction;
import com.isel.sincroserver.entities.RedLightInfraction;
import com.isel.sincroserver.entities.SpeedInfraction;
import com.isel.sincroserver.entities.VehicleAuthorization;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/radar")
@Qualifier("RadarController")
public class RadarController {
    private static final Logger logger = LogManager.getLogger(RadarController.class);
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
    public RadarController(Resources resources,
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

    @PostMapping(value="/insertSpeedInfraction", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertSpeedInfraction(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("speedInfraction", SpeedInfraction.class));

            if (map == null) {
                logger.error("insertSpeedInfraction - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("insertSpeedInfraction - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SpeedInfraction speedInfraction = (SpeedInfraction)map.get("speedInfraction");

        try {
            if (!mySQLService.insertSpeedInfraction(speedInfraction)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/insertRedLightInfraction", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertRedLightInfraction(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("redLightInfraction", RedLightInfraction.class));

            if (map == null) {
                logger.error("insertRedLightInfraction - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("insertRedLightInfraction - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        RedLightInfraction redLightInfraction = (RedLightInfraction)map.get("redLightInfraction");

        try {
            if (!mySQLService.insertRedLightInfraction(redLightInfraction)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/insertDistanceInfraction", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertDistanceInfraction(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("distanceInfraction", DistanceInfraction.class));

            if (map == null) {
                logger.error("insertDistanceInfraction - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("insertDistanceInfraction - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DistanceInfraction distanceInfraction = (DistanceInfraction)map.get("distanceInfraction");

        try {
            if (!mySQLService.insertDistanceInfraction(distanceInfraction)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
