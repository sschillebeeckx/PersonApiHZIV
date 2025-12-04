package be.abis.exercise;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import be.abis.exercise.util.DateUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonProdServiceTest {

    @Autowired
    PersonService ps;

    Company c;

    @BeforeEach
    public void setUp() {
        Address a = new Address("Diestsevest","32 bus 4b","3000","Leuven","B");
        c = new Company("Abis","016/455610","BE12345678",a);
    }

    @Test
    @Order(1)
    void person1IsJohn() {
        assertEquals("John",ps.findPerson(1).block().getFirstName());
    }

    @Test
    @Order(2)
    public void startSizeOfFileIs3() {
        int size = ps.getAllPersons().collectList().block().size();
        assertEquals(3,size);
    }

    @Test
    @Order(3)
    public void loginJohnCorrect() throws LoginException {
        assertEquals("John",ps.findPerson("jdoe@abis.be","def456").getFirstName());
    }

    @Test
    @Order(4)
    public void loginJohnWrong()  {
        assertThrows(LoginException.class, ()->ps.findPerson("jdoe@abis.be","sqdmkjfmsf"));
    }

    @Test
    @Order(6)
    public void addingJDoeAgainThrowsException() {
        Person p = new Person(4,"Jef","Doe", DateUtils.parse("12/11/2001"),"jdoe@abis.be","somepass","en",c);
        assertThrows(PersonAlreadyExistsException.class, ()-> ps.addPerson(p));
    }

    @Test
    @Order(7)
    public void addingNewPersonWorks() throws PersonAlreadyExistsException {
        Person newPerson = new Person(4,"Sandy","Schillebeeckx",DateUtils.parse("10/4/1978"),"sschillebeeckx@abis.be","mypass124","nl",c);
        ps.addPerson(newPerson);
    }

    @Test
    @Order(8)
    public void changePassWordOfAddedPerson() throws LoginException {
        Person p = ps.findPerson("sschillebeeckx@abis.be","mypass124");
        ps.changePassword(p,"blabla").block();
    }

    @Test
    @Order(9)
    public void deleteAddedPerson(){
        ps.deletePerson(4).block();
    }

}
