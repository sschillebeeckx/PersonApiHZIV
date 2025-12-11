package be.abis.exercise.controller;

import be.abis.exercise.dto.CountResult;
import be.abis.exercise.dto.PersonDTO;
import be.abis.exercise.dto.Persons;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.form.Login;
import be.abis.exercise.form.Password;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.mapper.PersonMapper;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("persons")
public class PersonController {

    @Autowired
    PersonService ps;
    @Autowired
    Validator validator;

    @PostMapping("login")
    public Mono<PersonDTO> findPersonByMailAndPwd(@RequestBody Login login) {
        Mono<Person> person = ps.findPerson(login.getEmail(), login.getPassword());
        return person.map(PersonMapper::toDTO);
    }

    @GetMapping("")
    public Flux<PersonDTO> getAllPersons() {
        Flux<Person> persons = ps.getAllPersons();
        return persons.map(PersonMapper::toDTO);
    }

    @GetMapping("{id}")
    public Mono<PersonDTO> findPerson(@PathVariable int id) {
        Mono<Person> person = ps.findPerson(id);
        return person.map(PersonMapper::toDTO);
    }

    /*@GetMapping(path = "/query")
    public Flux<Person> findPersonsByCompanyNameJSON(@RequestParam("compname") String compName) {
        return ps.findPersonsByCompanyName(compName);
    }*/

    @GetMapping(path = "/query", produces = MediaType.APPLICATION_XML_VALUE)
    public Mono<Persons> findPersonsByCompanyName(@RequestParam("compname") String compName) {
        return ps.findPersonsByCompanyName(compName)
                .map(p->PersonMapper.toDTO(p))
                .collectList()
                .map(list -> {
                    Persons persons = new Persons();
                    persons.setPersons(list);
                    return persons;
                });
    }


   /* @PostMapping("")
    public Mono<ResponseEntity<Object>> addPerson(@Valid @RequestBody Person p) {
       return ps.addPerson(p)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(PersonAlreadyExistsException.class, ex -> {
                    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                    problem.setTitle("Person already exists");
                    problem.setDetail(ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(problem));
                });

    } */

    @PostMapping("")
    public Mono<ResponseEntity<Object>> addPerson(@RequestBody PersonForm p) {

        Set<ConstraintViolation<PersonForm>> violations = validator.validate(p);
        if (!violations.isEmpty()) {
            Map<String, String> errors = violations.stream()
                    .collect(Collectors.toMap(
                            v -> v.getPropertyPath().toString(),
                            ConstraintViolation::getMessage
                    ));
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problem.setTitle("Validation Failed");
            problem.setProperty("errors", errors);

            return Mono.just(ResponseEntity.<ProblemDetail>badRequest().body(problem));
        }

        // business logic
        return ps.addPerson(PersonMapper.toPerson(p))
                .map(saved -> ResponseEntity.ok().body((Object) PersonMapper.toDTO(saved)))
                .onErrorResume(PersonAlreadyExistsException.class, ex -> {
                    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                    problem.setTitle("Person already exists");
                    problem.setDetail(ex.getMessage());
                    return Mono.just(ResponseEntity.<ProblemDetail>status(HttpStatus.CONFLICT).body(problem));
                });
    }

    @DeleteMapping("{id}")
    public Mono<Void> deletePerson(@PathVariable("id") int id) {
        return ps.deletePerson(id);
    }

    @PutMapping("{id}")
    public Mono<PersonDTO> changePasswordviaPerson(@PathVariable("id") int id, @Valid @RequestBody PersonForm person) {
       Mono<Person> p = ps.changePassword(PersonMapper.toPerson(person), person.getPassword());
        return p.map(PersonMapper::toDTO);
    }

	/*@PatchMapping("{id}/password")
	public Mono<Void> patchPassword(@PathVariable("id") int id, @RequestBody Password password) {
		Person p = ps.findPerson(id).block();
		System.out.println("changing password to newpswd= " + password.getPassword());
		return ps.changePassword(p, password.getPassword());
	}*/

    @PatchMapping("{id}/password")
    public Mono<PersonDTO> patchPassword(@PathVariable("id") int id, @Valid @RequestBody Password password) {
        return ps.findPerson(id)                  // returns Mono<Person>
                .flatMap(p -> {
                    System.out.println("changing password to newpswd= " + password.getPassword());
                    return ps.changePassword(p, password.getPassword()).map(PersonMapper::toDTO); // returns Mono<Void>
                });
    }

    @GetMapping("/count")
    public CountResult findNumberOfCourses(){
        Long count = ps.count();
        return new CountResult(count);
    }
}