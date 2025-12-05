package be.abis.exercise.dto;

import be.abis.exercise.model.Person;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name="persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persons {

	@XmlElement(name = "person")
	private  List<Person> persons;

	public  List<Person> getPersons() {
		return persons;
	}

	public  void setPersons(List<Person> persons) {
		this.persons = persons;
	}

}
