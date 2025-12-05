package be.abis.exercise;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import be.abis.exercise.util.DateUtils;
import be.abis.exercise.util.LoginPersonNonReactive;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonProdServiceTest {

    @Autowired
    PersonService ps;

    @Autowired
    LoginPersonNonReactive loginPersonNonReactive;

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
        assertEquals("John",ps.findPerson("jdoe@abis.be","def456").block().getFirstName());
    }

    @Test
    @Order(4)
    public void loginJohnWrong() throws LoginException {
      /*  Mono<Person> result = ps.findPerson("jdoe@abis.be","sqdmkjfmsf");
        StepVerifier.create(result)
                .expectErrorSatisfies(ex -> {
                    assertTrue(ex instanceof LoginException);
                    assertEquals("wrong email or password", ex.getMessage());
                })
                .verify(); */

        Throwable ex = assertThrows(RuntimeException.class, ()-> ps.findPerson("jdoe@abis.be","sqdmkjfmsf").block());
        assertTrue(ex.getCause() instanceof LoginException);
    }

    @Test
    @Order(5)
    public void loginJohnWrongWithAopTriggered() throws LoginException {
        assertThrows(LoginException.class,()-> loginPersonNonReactive.findPersonBlocking("jdoe@abis.be","sqdmkjfmsf"));
    }

    @Test
    @Order(6)
    public void addingJDoeAgainThrowsException() {
        Person p = new Person(4,"Jef","Doe", DateUtils.parse("12/11/2001"),"jdoe@abis.be","somepass","en",c);
        Throwable ex = assertThrows(RuntimeException.class, ()-> ps.addPerson(p).block());
        assertTrue(ex.getCause() instanceof PersonAlreadyExistsException);
    }

    @Test
    @Order(7)
    public void addingNewPersonWorks() throws PersonAlreadyExistsException {
        Person newPerson = new Person(4,"Sandy","Schillebeeckx",DateUtils.parse("10/4/1978"),"sschillebeeckx@abis.be","mypass124","nl",c);
        ps.addPerson(newPerson).block();
    }

    @Test
    @Order(8)
    public void changePassWordOfAddedPerson() throws LoginException {
        Person p = ps.findPerson("sschillebeeckx@abis.be","mypass124").block();
        ps.changePassword(p,"blabla").block();
    }

    @Test
    @Order(9)
    public void deleteAddedPerson(){
        ps.deletePerson(4).block();
    }




}
