package be.abis.exercise.service;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Person;
import be.abis.exercise.repository.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Profile("prod")
public class AbisPersonService implements PersonService {

    private PersonRepository personRepository;

    public AbisPersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Flux<Person> getAllPersons() {
        return Mono.fromCallable(personRepository::getAllPersons)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(list -> Flux.fromIterable(list));
    }

    @Override
    public Mono<Person> findPerson(int id) {
        return Mono.fromCallable(() -> personRepository.findPerson(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<Person> findPerson(String emailAddress, String passWord) {
        return Mono.justOrEmpty(personRepository.findPerson(emailAddress, passWord))
                .switchIfEmpty(Mono.error(new LoginException("wrong email or password")));
    }

    @Override
    public Mono<Void> addPerson(Person p) {
        return Mono.fromCallable(() -> {
            personRepository.addPerson(p); // your original method that throws a checked exception
            return null;   // Mono<Void> requires a return value
        });
    }

    @Override
    public Mono<Void> deletePerson(int id) {
       return Mono.fromRunnable(() -> personRepository.deletePerson(id));
    }

    @Override
    public Mono<Void> changePassword(Person p, String newPswd) {
        return Mono.fromRunnable(() ->personRepository.changePassword(p, newPswd));
    }

    @Override
    public Flux<Person> findPersonsByCompanyName(String compName) {
        return Mono.fromCallable(()->personRepository.findPersonsByCompanyName(compName))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(list -> Flux.fromIterable(list));
    }
}
