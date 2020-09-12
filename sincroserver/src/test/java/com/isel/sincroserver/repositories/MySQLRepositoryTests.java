package com.isel.sincroserver.repositories;

import com.isel.sincroserver.entities.Citizen;
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

    @Test
    void contextLoads() {
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(resources);
    }

    @Test
    void testFileResources() {

    }

    @Test
    void testRepositoryCitizenOps() throws SincroServerException {
        Citizen citizen1 = new Citizen(0,
                "Rafael",
                "Gomes",
                "Nobre",
                "123456789",
                "251489558",
                "933417044",
                "a39267@alunos.isel.pt");

        Citizen citizen2 = new Citizen(0,
                "Pedro",
                "Miguel",
                "Santos",
                "234567891",
                "198765432",
                "911111111",
                "11111@gmail.com");

        Assertions.assertNull(repository.getCitizen(citizen1.getCc_number()));

        Assertions.assertEquals(1, repository.insertCitizen(citizen1));

        Assertions.assertEquals(citizen1, repository.getCitizen(citizen1.getCc_number()));

        citizen1.setEmail("rafael.gomes.nobre@gmail.com");

        Assertions.assertEquals(1, repository.updateCitizenEmail(citizen1.getCc_number(), citizen1.getEmail()));

        Assertions.assertEquals(citizen1.getEmail(), repository.getCitizen(citizen1.getCc_number()).getEmail());

        citizen1.setPhone_number("966666666");

        Assertions.assertEquals(1, repository.updateCitizenPhoneNumber(citizen1.getCc_number(), citizen1.getPhone_number()));

        Assertions.assertEquals(citizen1.getPhone_number(), repository.getCitizen(citizen1.getCc_number()).getPhone_number());

        Assertions.assertEquals(1, repository.insertCitizen(citizen2));

        Assertions.assertEquals(2, repository.getCitizens().size());

        Assertions.assertEquals(1, repository.deleteCitizen(citizen2.getCc_number()));

        Assertions.assertEquals(1, repository.deleteCitizen(citizen1.getCc_number()));
    }
}
