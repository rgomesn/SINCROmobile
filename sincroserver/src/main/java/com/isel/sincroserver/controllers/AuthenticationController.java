package com.isel.sincroserver.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isel.sincroserver.config.JwtUserDetailsService;
import com.isel.sincroserver.config.JwtTokenUtils;
import com.isel.sincroserver.entities.Citizen;
import com.isel.sincroserver.entities.User;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.services.MySQLService;
import com.isel.sincroserver.utils.Utils;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/authenticate")
@Qualifier("AuthenticationController")
public class AuthenticationController {
    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);
    private final JwtTokenUtils jwtTokenUtils;
    private final JwtUserDetailsService userDetailsService;
    @Qualifier("MySQLService")
    private final MySQLService mySQLService;
    @Qualifier("Utils")
    private final Utils utils;

    public AuthenticationController(JwtTokenUtils jwtTokenUtils, JwtUserDetailsService userDetailsService, MySQLService mySQLService, Utils utils) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
        this.mySQLService = mySQLService;
        this.utils = utils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody ObjectNode objectNode) {
        Map<String, Object> map;
        try {
            map = utils.getRequestParameters(objectNode,
                    (List<String> errorList) -> errorList.forEach(str -> logger.error("json object with name \"" + str + "\" required in request body")),
                    new Pair<>("username", String.class),
                    new Pair<>("password", String.class));

            if (map == null) {
                logger.error("createAuthenticationToken - missing parameters");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("beginRegistration - could not read request body: " + objectNode.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = (String)map.get("username"), password = (String)map.get("password");

        try {
            if (!userDetailsService.authenticate(username, password)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        User user = null;
        Citizen citizen = null;
        try {
            user = userDetailsService.loadUser(username);
            citizen = mySQLService.getCitizen(user.getCc_number());
        } catch (SincroServerException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("token", jwtTokenUtils.generateToken(userDetails));
        res.put("user", user);
        res.put("citizen", citizen);
        return ResponseEntity.ok(res);
    }
}