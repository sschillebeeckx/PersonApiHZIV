package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonService {
    Flux<Person> getAllPersons();
    Mono<Person> findPerson(int id);
    Person findPerson(String emailAddress, String passWord) throws LoginException;
    void addPerson(Person p) throws PersonAlreadyExistsException;
    Mono<Void> deletePerson(int id);
    Mono<Void> changePassword(Person p, String newPswd);
    Flux<Person> findPersonsByCompanyName(String compName);
}
