package com.isel.sincroserver.external;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isel.sincroserver.entities.Citizen;
import com.isel.sincroserver.entities.Vehicle;
import com.isel.sincroserver.interfaces.external.ExternalCitizenDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("DummyExternalCitizenData")
public class DummyExternalCitizenDataProvider implements ExternalCitizenDataProvider {
    private final JsonObject citizens;
    Gson gson;

    private DummyExternalCitizenDataProvider() throws IOException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource("citizens.json");

        File file = new File(url.toURI());

        InputStream is = new FileInputStream(file);

        gson = new Gson();

        JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);

        /*ObjectMapper om = new ObjectMapper();
        JavaType type = om.getTypeFactory().constructParametricType(HashMap.class, String.class, HashMap.class);
        citizens = om.readValue(is, type);*/

        citizens = obj;
    }

    @Override
    public Citizen obtainCitizenInformation(String cc_number) {
        if (citizens.has(cc_number)) {
            return gson.fromJson(citizens.getAsJsonObject(cc_number).get("citizen"), Citizen.class);
        }
        return null;
    }

    @Override
    public List<Vehicle> obtainCitizenVehicles(String cc_number) {
        if (citizens.has(cc_number)) {
            return gson.fromJson(citizens.getAsJsonObject(cc_number).get("vehicles"), List.class);
        }
        return null;
    }
}
