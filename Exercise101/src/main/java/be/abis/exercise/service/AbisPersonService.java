package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.NoPersonsFoundException;
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
    public Mono<Person> findPerson(String emailAddress, String passWord) {
        return Mono.justOrEmpty(personRepository.findByEmailAddressAndPassword(emailAddress, passWord))
                .switchIfEmpty(Mono.error(new LoginException("wrong email or password")));
    }

    @Override
    @Transactional
    public Mono<Person> addPerson(Person p) {
        Company c = p.getCompany();
        if (c != null) {
            Company foundComp = companyRepository.getByNameAndTown(c.getName(), c.getAddress().getTown());
            if(foundComp!=null) p.setCompany(foundComp);
        }
        return Mono.just(personRepository.save(p));
    }

    @Override
    public Mono<Void> deletePerson(int id) {
       return Mono.fromRunnable(() -> personRepository.deleteById(id));
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


}
