package com.isel.sincroserver.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isel.sincroserver.entities.Citizen;
import com.isel.sincroserver.entities.User;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.external.ExternalCitizenDataProvider;
import com.isel.sincroserver.interfaces.resources.Resources;
import com.isel.sincroserver.services.EmailService;
import com.isel.sincroserver.services.MySQLService;
import com.isel.sincroserver.services.RegistrationService;
import com.isel.sincroserver.utils.Utils;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping("/register")
@Qualifier("RegistrationController")
public class RegistrationController {
    private static final Logger logger = LogManager.getLogger(RegistrationController.class);
    @Qualifier("FileResources")
    private final Resources resources;
    @Qualifier("DummyExternalCitizenDataProvider")
    private final ExternalCitizenDataProvider externalCitizenDataProvider;
    @Qualifier("RegistrationService")
    private final RegistrationService registrationService;
    @Qualifier("EmailService")
    private final EmailService emailService;
    @Qualifier("MySQLService")
    private final MySQLService mySQLService;
    @Qualifier("Utils")
    private final Utils utils;

    public RegistrationController(Resources resources,
                                  ExternalCitizenDataProvider externalCitizenDataProvider,
                                  RegistrationService registrationService,
                                  EmailService emailService,
                                  MySQLService mySQLService, Utils utils) {
        this.resources = resources;
        this.externalCitizenDataProvider = externalCitizenDataProvider;
        this.registrationService = registrationService;
        this.emailService = emailService;
        this.mySQLService = mySQLService;
        this.utils = utils;
    }

    @PostMapping(value="/beginRegistration", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Citizen> beginRegistration(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("cc_number", String.class));

            if (map == null) {
                logger.error("beginRegistration - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("beginRegistration - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String cc_number = (String)map.get("cc_number");

        Citizen citizen = externalCitizenDataProvider.obtainCitizenInformation((String)map.get("cc_number"));

        if (citizen != null) {
            return new ResponseEntity<>(citizen, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value="/registerWithEmail", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerWithEmail(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class));

            if (map == null) {
                logger.error("registerWithEmail - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("registerWithEmail - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");

        String code = registrationService.createRegistrationCode(citizen);

        emailService.sendEmail(citizen, code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/verifyRegistrationCode", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyRegistrationCode(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("citizen", Citizen.class),
                    new Pair<>("inputCode", String.class));

            if (map == null) {
                logger.error("verifyRegistrationCode - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("verifyRegistrationCode - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Citizen citizen = (Citizen)map.get("citizen");
        String inputCode = (String)map.get("inputCode");

        if (!registrationService.verifyVerificationCode(citizen, inputCode)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            if (!mySQLService.insertCitizen(citizen) || !mySQLService.insertVehicles(externalCitizenDataProvider.obtainCitizenVehicles(citizen.getCc_number()), citizen)) {
                logger.error("Could not insert citizen with cc_number: " + citizen.getCc_number());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/registerLoginAndPassword", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerLoginAndPassword(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map = null;

        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<String, Class<?>>("user", User.class));

            if (map == null) {
                logger.error("getInfractions - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("getInfractions - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = (User)map.get("user");

        try {
            if (!mySQLService.insertUser(user)) {
                logger.error("Could not insert user with username: " + user.getUsername());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SincroServerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
