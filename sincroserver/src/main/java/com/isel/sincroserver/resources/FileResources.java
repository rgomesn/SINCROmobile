package com.isel.sincroserver.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.exception.SincroServerExceptionType;
import com.isel.sincroserver.interfaces.resources.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("FileResources")
public class FileResources implements Resources {
    private static final Logger logger = LogManager.getLogger(FileResources.class);
    private Resource messages = new Resource("messages.properties", null, null),
            errorMessages = new Resource("errorMessages.properties", null, null),
            queries = new Resource("queries.json", null, null);
    private HashMap<String, Object> resourcesCache = new HashMap<String, Object>();

    @Override
    public String getMessage(String messageId) throws SincroServerException {
        return (String) getPropertiesResource(messageId, messages);
    }

    @Override
    public String getErrorMessage(String errorMessageId) throws SincroServerException {
        return (String) getPropertiesResource(errorMessageId, errorMessages);
    }

    @Override
    public String getQuery(String queryId) throws SincroServerException {
        return String.join("", this.<List<String>>getJSONResource(queryId, queries));
    }

    private InputStream getResource(Resource resource) throws FileNotFoundException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource(resource.resourceName);

        if (url == null) {
            throw new FileNotFoundException(resource.resourceName);
        }

        File file = new File(url.toURI());

        if (resource.resourceLastReadAt != null && resource.resourceLastReadAt.equals(new Date(file.lastModified()))) {
            return null;
        }

        InputStream is = new FileInputStream(file);

        resource.resourceLastReadAt = new Date(file.lastModified());

        return is;
    }

    private String getPropertiesResource(String id, Resource resource) throws SincroServerException {
        try {
            InputStream is = getResource(resource);

            if (is != null) {
                Properties properties = new Properties();
                properties.load(is);
                properties.forEach((key, value) -> resourcesCache.put((String) key, value));
            }

            if (resourcesCache.containsKey(id)) {
                return (String)resourcesCache.get(id);
            }

            throw new SincroServerException("Failed to load resource with id \"" + id + "\". No cache.", SincroServerExceptionType.RESOURCE_NOT_FOUND);
        } catch (URISyntaxException | IOException e) {
            if (resourcesCache.containsKey(id)) {
                logger.warn("Resource \"" + resource.resourceName + " not found, but property: " + id + " is cached");
                return (String)resourcesCache.get(id);
            }

            throw new SincroServerException("Failed to load resource with id \"" + id + "\". No cache.", e, SincroServerExceptionType.FILE_ERROR);
        }
    }

    private <T> T getJSONResource(String id, Resource resource) throws SincroServerException {
        try {
            InputStream is = getResource(resource);

            if (is != null) {
                ObjectMapper om = new ObjectMapper();
                //JavaType type = om.getTypeFactory().constructParametricType(HashMap.class, String.class, T);
                Map<String, T> map = om.readValue(is, new TypeReference<Map<String, T>>(){});
                resourcesCache.putAll(map);
            }

            if (resourcesCache.containsKey(id)) {
                return (T)resourcesCache.get(id);
            }

            throw new SincroServerException("Failed to load resource with id \"" + id + "\". No cache.", SincroServerExceptionType.RESOURCE_NOT_FOUND);
        } catch (IOException | URISyntaxException e) {
            if (resourcesCache.containsKey(id)) {
                logger.warn("Resource \"" + resource.resourceName + " not found, but property: " + id + " is cached");
                return (T)resourcesCache.get(id);
            }

            throw new SincroServerException("Failed to load resource with id \"" + id + "\". No cache.", e, SincroServerExceptionType.FILE_ERROR);
        }
    }
}