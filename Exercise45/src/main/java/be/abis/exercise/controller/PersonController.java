package be.abis.exercise.controller;

import be.abis.exercise.dto.Persons;
import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.form.Login;
import be.abis.exercise.form.Password;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonController {

	@Autowired
	PersonService ps;

	@PostMapping("login")
	public Person findPersonByMailAndPwd(@RequestBody Login login) {
		Person p=null;
		try {
			p=ps.findPerson(login.getEmail(), login.getPassword());
		} catch (LoginException e){}
		return p;
	}

	@GetMapping("")
	public List<Person> getAllPersons() {
		return ps.getAllPersons();
	}

	@GetMapping("{id}")
	public Person findPerson(@PathVariable int id) {
		return ps.findPerson(id);
	}

	@GetMapping(path="/query", produces= MediaType.APPLICATION_XML_VALUE)
	public Persons findPersonsByCompanyName(@RequestParam("compname") String compName) {
		Persons persons= new Persons();
		persons.setPersons(ps.findPersonsByCompanyName(compName));
		return persons;
	}

	@PostMapping("")
	public void addPerson(@RequestBody Person p)  {
        try {
            ps.addPerson(p);
        } catch (PersonAlreadyExistsException e) {}
    }

	@DeleteMapping("{id}")
	public void deletePerson(@PathVariable("id") int id) {
		ps.deletePerson(id);
	}


	@PutMapping("{id}")
	public void changePasswordviaPerson(@PathVariable("id") int id, @RequestBody Person person) {
		System.out.println("changing password to newpswd= " + person.getPassword());
		ps.changePassword(person, person.getPassword());

	}

	@PatchMapping("{id}/password")
	public void patchPassword(@PathVariable("id") int id, @RequestBody Password password) {
		Person p = ps.findPerson(id);
		System.out.println("changing password to newpswd= " + password.getPassword());
		ps.changePassword(p, password.getPassword());

	}



}