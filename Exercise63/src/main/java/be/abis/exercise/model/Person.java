package be.abis.exercise.model;

import be.abis.exercise.util.LocalDateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"personId","firstName","lastName","birthDate","emailAddress","password","language","company"})
public class Person {

	private int personId;
	private String firstName;
	@XmlElement(name="lastname")
	private String lastName;
	@JsonFormat(pattern="d/M/yyyy")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate birthDate;
	private String emailAddress;
	private String password;
	private String language;
	private Company company;

	public Person(){}

	public Person(int personId, String firstName, String lastName, LocalDate birthDate, String emailAddress, String password, String language, Company company) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate=birthDate;
		this.emailAddress = emailAddress;
		this.password = password;
		this.language = language;
		this.company = company;
	}

	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public String toString() {
		return "Person with id " + personId + ", " + firstName + " "+ lastName + ", works for " +company.getName() + " in " + company.getAddress().getTown();
	}

	

}
