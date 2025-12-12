package be.abis.exercise.service;

import be.abis.exercise.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonService {
    Flux<Person> getAllPersons();
    Mono<Person> findPerson(int id);
    Mono<Person> findPersonByEmail(String email);
    Mono<Person> findPerson(String emailAddress, String passWord);
    Mono<Person> addPerson(Person p);
    Mono<Void> deletePerson(int id);
    Mono<Person> changePassword(Person p, String newPswd);
    Flux<Person> findPersonsByCompanyName(String compName);
    List<String> findHobbiesForPerson(int id);
    void addHobbyForPerson(int personId, String hobby);
    Long count();
}
