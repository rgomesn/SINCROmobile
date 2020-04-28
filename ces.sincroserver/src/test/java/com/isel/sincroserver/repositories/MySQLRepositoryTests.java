package com.isel.sincroserver.repositories;

import com.isel.sincroserver.entities.Driver;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;
import com.isel.sincroserver.interfaces.resources.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MySQLRepositoryTests {
    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("MySQLRepository")
    Repository repository;

    @Autowired
    @Qualifier("FileResources")
    Resources resources;

    Driver referenceDriver;

    {
        referenceDriver = new Driver(0,
                                     "Rafael",
                                     "Gomes",
                                     "Nobre",
                                     "123456789",
                                     "251489558",
                                     "933417044",
                                     "a39267@alunos.isel.pt");
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(resources);
    }

    @Test
    void testFileResources() {

    }

    @Test
    void testRepositoryDriverOps() throws SincroServerException {
        Assertions.assertNull(referenceDriver.getDriver(repository));

        Assertions.assertEquals(1, referenceDriver.insertDriver(repository));

        Driver dr1 = new Driver(0,
                "",
                "",
                "",
                "123456789",
                "",
                "",
                "");

        Driver dr2 = new Driver(0,
                "",
                "",
                "",
                "",
                "251489558",
                "",
                "");

        Driver dr3 = new Driver(0,
                "",
                "",
                "",
                "",
                "",
                "933417044",
                "");

        Driver dr4 = new Driver(0,
                "",
                "",
                "",
                "",
                "",
                "",
                "a39267@alunos.isel.pt");

        Assertions.assertEquals(referenceDriver, dr1.getDriver(repository));
        Assertions.assertEquals(referenceDriver, dr2.getDriver(repository));
        Assertions.assertEquals(referenceDriver, dr3.getDriver(repository));
        Assertions.assertEquals(referenceDriver, dr4.getDriver(repository));

        referenceDriver.setEmail("rafael.gomes.nobre@gmail.com");

        Assertions.assertEquals(1, referenceDriver.updateDriver(repository));

        Assertions.assertNull(dr4.getDriver(repository));

        dr4.setEmail("rafael.gomes.nobre@gmail.com");

        Assertions.assertEquals(referenceDriver, dr4.getDriver(repository));

        Assertions.assertEquals(1, referenceDriver.deleteDriver(repository));

        Assertions.assertEquals(List.of(), Driver.getDrivers(repository));
    }
}
