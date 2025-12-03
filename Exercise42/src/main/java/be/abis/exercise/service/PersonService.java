package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> getAllPersons();
    Person findPerson(int id);
    Person findPerson(String emailAddress, String passWord) throws LoginException;
    void addPerson(Person p) throws PersonAlreadyExistsException;
    void deletePerson(int id);
    void changePassword(Person p, String newPswd);
}
