package be.abis.exercise;

import be.abis.exercise.model.Person;
import be.abis.exercise.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PersonRepositoryTest {

	@Autowired
	PersonRepository pr;

	@Test
	public void startSizeOfFileIs3() {
		int size = pr.getAllPersons().size();
		assertEquals(3,size);
	}
	
	@Test
	public void personViaMailAndPwdisMary() {
		Person p = pr.findPerson("mjones@abis.be","abc123");
		assertEquals("Mary",p.getFirstName());
	}

	@Test
	public void thereAre2PersonsWorkingForAbis() {
		assertEquals(2,pr.findPersonsByCompanyName("Abis").size());
	}

}
