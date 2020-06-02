package com.isel.sincroserver.interfaces.controllers;

import org.springframework.web.bind.annotation.PostMapping;

public interface Controller {
    @PostMapping("/login")
    void login(String citizens_licence, String cc);
}
