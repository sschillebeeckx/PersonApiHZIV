package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Person;
import be.abis.exercise.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbisPersonService implements PersonService {

    private PersonRepository personRepository;

    public AbisPersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    @Override
    public Person findPerson(int id) {
        return personRepository.findPerson(id);
    }

    @Override
    public Person findPerson(String emailAddress, String passWord) throws LoginException {
        Person p = personRepository.findPerson(emailAddress, passWord);
        if (p == null) {throw new LoginException("wrong email or password");}
        return p;
    }

    @Override
    public void addPerson(Person p) throws PersonAlreadyExistsException {
        personRepository.addPerson(p);
    }

    @Override
    public void deletePerson(int id) {
       personRepository.deletePerson(id);
    }

    @Override
    public void changePassword(Person p, String newPswd) {
        personRepository.changePassword(p, newPswd);
    }
}
