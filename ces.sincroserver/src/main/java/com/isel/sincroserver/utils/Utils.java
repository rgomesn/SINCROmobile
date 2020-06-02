package com.isel.sincroserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Qualifier("Utils")
public class Utils {
    private ObjectMapper om = new ObjectMapper();

    public boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str);
    }

    private <T> T readObject(String json, Class<T> klass) throws JsonProcessingException {
        JavaType javaType = om.constructType(klass);
        return om.readValue(json, javaType);
    }

    @SafeVarargs
    public final Map<String, Object> getRequestParameters(ObjectNode objectNode,
                                                          Consumer<List<String>> errors,
                                                          Pair<String, Class<?>>... params) throws JsonProcessingException {
        HashMap<String, Object> res = new HashMap<>();
        List<String> errorList = new ArrayList<>();

        for (Pair<String, Class<?>> param : params) {
            JsonNode node = objectNode.get(param.getKey());

            if (node == null) {
                errorList.add(param.getKey());
            } else {
                res.put(param.getKey(), readObject(node.toString(), param.getValue()));
            }
        }

        if (!errorList.isEmpty()) {
            errors.accept(errorList);
            return null;
        }

        return res;
    }
}