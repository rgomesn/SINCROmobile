package com.isel.sincroserver.services;

import com.isel.sincroserver.entities.Citizen;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Qualifier("RegistrationService")
public class RegistrationService {
    Map<String, String> verificationCodes = new HashMap<>();

    public String createRegistrationCode(Citizen citizen) {
        Random r = new Random();
        String code = String.valueOf(r.ints(100000000, 1000000000).findFirst().getAsInt());
        verificationCodes.put(citizen.getCc_number(), code);
        return code;
    }

    public boolean verifyVerificationCode(Citizen citizen, String inputCode) {
        String code = verificationCodes.get(citizen.getCc_number());
        return code.equals(inputCode);
    }
}