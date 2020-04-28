package com.isel.sincroserver.resources;

import com.isel.sincroserver.interfaces.resources.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileRepositoryTests {
    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("FileResources")
    Resources resources;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(resources);
    }

    @Test
    void testFileResources() throws URISyntaxException, IOException {
        URL url = getClass().getClassLoader().getResource("src/main/resources");
        File resources_dir = new File(url.toURI());

        Runtime.getRuntime().exec("mkdir temp", null, resources_dir);
        Runtime.getRuntime().exec("move queries.json temp", null, resources_dir);


    }
}