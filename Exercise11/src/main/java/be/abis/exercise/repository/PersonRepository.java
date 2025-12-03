package be.abis.exercise.repository;

import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Person;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface PersonRepository {
	
	List<Person> getAllPersons();
	Person findPerson(int id);
	Person findPerson(String emailAddress, String passWord);
	void addPerson(Person p) throws PersonAlreadyExistsException;
	void deletePerson(int id);
	void changePassword(Person p, String newPswd);
}
