package be.abis.exercise;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("dev")
class PersonDevServiceTest {

    @Autowired
    PersonService ps;

    @Test
    public void loginReturnsSandy() throws LoginException {
        assertEquals("Sandy",ps.findPerson("sschillebeeckx@abis.be","abis123").getFirstName());
    }





}
