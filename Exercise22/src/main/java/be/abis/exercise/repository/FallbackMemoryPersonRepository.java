package be.abis.exercise.repository;

import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@Repository
@ConditionalOnMissingBean(FilePersonRepository.class)
public class FallbackMemoryPersonRepository implements PersonRepository {

    private ArrayList<Person> allPersons = new ArrayList<Person>();

    public FallbackMemoryPersonRepository()  {

        System.out.println("using fallback");

        Address a1 = new Address("Diestsevest","32 bus 4b","3000","Leuven","B");
        Address a2 = new Address("Medialaan","50","1800","Vilvoorde","B");

        Company c1 = new Company("Abis","016/245610","BE 0428.407.725",a1);
        Company c2 = new Company("Oracle","02/7191211","BE 0440.966.354",a2);

        Person p1 = new Person(1,"John","Doe",35,"jdoe@abis.be","def456", "nl",c1);
        Person p2 = new Person(2,"Mary","Jones",27,"mjones@abis.be","abc123", "fr",c1);
        Person p3 = new Person(3,"Bob","Smith",53,"bob.smith@oracle.com","abc986", "en",c2);

        allPersons.addAll(Arrays.asList(new Person[]{p1,p2,p3}));

    }

    @Override
    public ArrayList<Person> getAllPersons() {
        return allPersons;
    }

    @Override
    public Person findPerson(int id) {
        return allPersons.stream().filter(p->p.getPersonId()==id).findFirst().orElse(null);
    }

    @Override
    public Person findPerson(String emailAddress, String passWord) {
        return allPersons.stream().filter(p->p.getEmailAddress().equalsIgnoreCase(emailAddress)&&p.getPassword().equalsIgnoreCase(passWord)).findFirst().orElse(null);
    }

    @Override
    public void addPerson(Person p) throws PersonAlreadyExistsException {
        boolean b = false;
        Iterator<Person> iter = allPersons.iterator();
        while (iter.hasNext()) {
            Person pers = iter.next();
            if (pers.getEmailAddress().equalsIgnoreCase(p.getEmailAddress())) {
                throw new PersonAlreadyExistsException("you were already registered, login please");
            } else {
                b = true;
            }
        }
        if (b) {allPersons.add(p);}
    }

    @Override
    public void deletePerson(int id) {
        allPersons.remove(this.findPerson(id));
    }

    @Override
    public void changePassword(Person p, String newPswd) {
        this.findPerson(p.getPersonId()).setPassword(newPswd);
    }
}
