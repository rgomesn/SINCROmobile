package com.isel.sincroserver.external;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isel.sincroserver.entities.Citizen;
import com.isel.sincroserver.interfaces.external.ExternalCitizenDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("DummyExternalCitizenData")
public class DummyExternalCitizenDataProvider implements ExternalCitizenDataProvider {
    private final Map<String, Citizen> citizens;

    private DummyExternalCitizenDataProvider() throws IOException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource("citizens.json");

        File file = new File(url.toURI());

        InputStream is = new FileInputStream(file);

        ObjectMapper om = new ObjectMapper();
        JavaType type = om.getTypeFactory().constructParametricType(HashMap.class, String.class, Citizen.class);
        citizens = om.readValue(is, type);
    }

    @Override
    public Citizen obtainCitizenInformation(String cc_number) {
        return citizens.get(cc_number);
    }
}
