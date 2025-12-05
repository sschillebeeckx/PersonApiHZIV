package be.abis.exercise.controller;

import be.abis.exercise.dto.Persons;
import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.form.Login;
import be.abis.exercise.form.Password;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("persons")
public class PersonController {

    @Autowired
    PersonService ps;

    @PostMapping("login")
    public Mono<Person> findPersonByMailAndPwd(@RequestBody Login login) throws LoginException {
        return ps.findPerson(login.getEmail(), login.getPassword());
    }

    @GetMapping("")
    public Flux<Person> getAllPersons() {
        return ps.getAllPersons();
    }

    @GetMapping("{id}")
    public Mono<Person> findPerson(@PathVariable int id) {
        return ps.findPerson(id);
    }

    @GetMapping(path = "/query", produces = MediaType.APPLICATION_XML_VALUE)
    public Mono<Persons> findPersonsByCompanyName(@RequestParam("compname") String compName) {
        return ps.findPersonsByCompanyName(compName)
                .collectList()
                .map(list -> {
                    Persons persons = new Persons();
                    persons.setPersons(list);
                    return persons;
                });
    }

	/*@GetMapping(path = "/query", produces = MediaType.APPLICATION_XML_VALUE)
	public Flux<Person> findPersonsByCompanyName(@RequestParam("compname") String compName) {
		return ps.findPersonsByCompanyName(compName);
	}*/

    @PostMapping(path = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Mono<ResponseEntity<Object>> addPerson(@RequestBody Person p) {
       return ps.addPerson(p)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(PersonAlreadyExistsException.class, ex -> {
                    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                    problem.setTitle("Person already exists");
                    problem.setDetail(ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(problem));
                });

    }

    @DeleteMapping("{id}")
    public Mono<Void> deletePerson(@PathVariable("id") int id) {
        return ps.deletePerson(id);
    }

    @PutMapping("{id}")
    public Mono<Void> changePasswordviaPerson(@PathVariable("id") int id, @RequestBody Person person) {
        System.out.println("changing password to newpswd= " + person.getPassword());
        return ps.changePassword(person, person.getPassword());
    }

	/*@PatchMapping("{id}/password")
	public Mono<Void> patchPassword(@PathVariable("id") int id, @RequestBody Password password) {
		Person p = ps.findPerson(id).block();
		System.out.println("changing password to newpswd= " + password.getPassword());
		return ps.changePassword(p, password.getPassword());
	}*/

    @PatchMapping("{id}/password")
    public Mono<Void> patchPassword(@PathVariable("id") int id, @RequestBody Password password) {
        return ps.findPerson(id)                  // returns Mono<Person>
                .flatMap(p -> {
                    System.out.println("changing password to newpswd= " + password.getPassword());
                    return ps.changePassword(p, password.getPassword()); // returns Mono<Void>
                });
    }

}