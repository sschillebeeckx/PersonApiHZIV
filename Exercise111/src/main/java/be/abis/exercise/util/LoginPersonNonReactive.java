package be.abis.exercise.util;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginPersonNonReactive {

    @Autowired
    PersonService ps;

    public Person findPersonBlocking(String email, String pass) throws LoginException {
        try {
            return ps.findPerson(email, pass).block();
        } catch (RuntimeException ex) {
            System.out.println("in catch");
            Throwable cause = ex.getCause();
            if (cause instanceof LoginException) {
                throw (LoginException) cause;  // unwrap so assertThrows sees it
            }
            throw ex; // rethrow other exceptions
        }
    }

}
