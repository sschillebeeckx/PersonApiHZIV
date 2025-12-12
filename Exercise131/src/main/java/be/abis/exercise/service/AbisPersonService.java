package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.NoPersonsFoundException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.exception.PersonNotFoundException;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import be.abis.exercise.repository.CompanyJpaRepository;
import be.abis.exercise.repository.PersonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class AbisPersonService implements PersonService {

    private PersonJpaRepository personRepository;
    @Autowired
    private CompanyJpaRepository companyRepository;

    public AbisPersonService(PersonJpaRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Flux<Person> getAllPersons() {
        return Mono.fromCallable(personRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(list -> Flux.fromIterable(list));
    }

    @Override
    public Mono<Person> findPerson(int id) {
        return Mono.justOrEmpty(personRepository.findByPersonId(id))
                .switchIfEmpty(Mono.error(new PersonNotFoundException("person with id " + id + " not found")));
    }

    @Override
    public Mono<Person> findPersonByEmail(String email) {
        return Mono.just(personRepository.findByEmailAddress(email));
    }

    @Override
    public Mono<Person> findPerson(String emailAddress, String passWord) {
        return Mono.justOrEmpty(personRepository.findByEmailAddressAndPassword(emailAddress, passWord))
                .switchIfEmpty(Mono.error(new LoginException("wrong email or password")));
    }

    @Override
    @Transactional
    public Mono<Person> addPerson(Person p) {
        Person foundPerson = personRepository.findByEmailAddress(p.getEmailAddress());
        if (foundPerson!=null) return Mono.error(new PersonAlreadyExistsException("You are already registered. Please login"));
        Company c = p.getCompany();
        if (c != null) {
            Company foundComp = companyRepository.getByNameAndTown(c.getName(), c.getAddress().getTown());
            if(foundComp!=null) p.setCompany(foundComp);
        }
        return Mono.just(personRepository.save(p));
    }

    @Override
    @Transactional
    public Mono<Void> deletePerson(int id) {
       Person p= personRepository.findByPersonId(id);
       companyRepository.deleteById(p.getCompany().getId());
       personRepository.deleteById(id);
       return Mono.empty();
    }

    @Override
    public Mono<Person> changePassword(Person p, String newPswd) {
        Person person = personRepository.findByEmailAddress(p.getEmailAddress());
        person.setPassword(newPswd);
        return Mono.just(personRepository.save(person));
    }

    @Override
    public Flux<Person> findPersonsByCompanyName(String compName) {
        return Mono.fromCallable(() -> personRepository.findPersonsByCompanyName(compName))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.error(
                        new NoPersonsFoundException("No persons found for company: " + compName)
                ));
    }

    @Override
    public Long count() {
        return personRepository.count();
    }

    @Override
    @Transactional
    public void addHobbyForPerson(int personId, String hobby) {
        int hno = personRepository.getLastHnoForPerson(personId);
        personRepository.addHobbyForPerson(personId,hno+1,hobby);
        //personRepository.flush();
    }

    @Override
    public List<String> findHobbiesForPerson(int id) {
        return personRepository.findByPersonId(id).getHobbies();
    }

}
