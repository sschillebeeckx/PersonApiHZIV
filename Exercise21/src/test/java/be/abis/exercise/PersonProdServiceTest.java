package be.abis.exercise;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import be.abis.exercise.service.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals("John",ps.findPerson(1).getFirstName());
    }

    @Test
    @Order(2)
    public void startSizeOfFileIs3() {
        int size = ps.getAllPersons().size();
        assertEquals(3,size);
    }

    @Test
    @Order(3)
    public void loginJohnCorrect() throws LoginException {
        assertEquals("John",ps.findPerson("jdoe@abis.be","def456").getFirstName());
    }

    @Test
    @Order(4)
    public void loginJohnWrong() {
        assertThrows(LoginException.class, ()-> ps.findPerson("jdoe@abis.be","sqdmkjfmsdf"));
    }

    @Test
    @Order(5)
    public void addingJohnAgainThrowsException() {
        Person p = new Person(4,"Jef","Doe",28,"jdoe@abis.be","somepass","en",c);
        assertThrows(PersonAlreadyExistsException.class, ()-> ps.addPerson(p));
    }

    @Test
    @Order(6)
    public void addingNewPersonWorks() throws PersonAlreadyExistsException {
        Person newPerson = new Person(4,"Sandy","Schillebeeckx",42,"sschillebeeckx@abis.be","mypass124","nl",c);
        ps.addPerson(newPerson);
    }

    @Test
    @Order(7)
    public void changePassWordOfAddedPerson() throws LoginException {
        Person p = ps.findPerson("sschillebeeckx@abis.be","mypass124");
        ps.changePassword(p,"blabla");
    }

    @Test
    @Order(8)
    public void deleteAddedPerson(){
        ps.deletePerson(4);
    }




}
