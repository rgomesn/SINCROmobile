package com.isel.sincroserver.controllers;

import com.isel.sincroserver.interfaces.controllers.Controller;
import com.isel.sincroserver.interfaces.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/sincroapp")
@Qualifier("SincroAppController")
public class SincroAppController implements Controller {
    @Autowired
    @Qualifier("FileResources")
    Resources resources;

    @Override()
    @GetMapping(path = "/login")
    public void login(String drivers_licence, String cc) {

    }

    @GetMapping(path = "/insertdriver")
    public void insertDriver(String drivers_licence, String cc) {

    }
}