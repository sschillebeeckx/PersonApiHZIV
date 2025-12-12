package be.abis.exercise.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name="persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persons {

	@XmlElement(name = "person")
	private  List<PersonDTO> persons;

	public  List<PersonDTO> getPersons() {
		return persons;
	}

	public  void setPersons(List<PersonDTO> persons) {
		this.persons = persons;
	}

}
