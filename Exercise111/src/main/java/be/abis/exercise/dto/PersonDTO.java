package be.abis.exercise.dto;

import be.abis.exercise.util.LocalDateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDTO {

    private String firstName;
    @XmlElement(name="lastname")
    private String lastName;
    private String email;
    @JsonFormat(pattern="d/M/yyyy")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate birthDate;
    private String companyName;
    private String companyTown;

    public PersonDTO(){}
    public PersonDTO(String firstName, String lastName, String email, LocalDate birthDate, String companyName, String companyTown) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.companyName = companyName;
        this.companyTown = companyTown;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTown() {
        return companyTown;
    }

    public void setCompanyTown(String companyTown) {
        this.companyTown = companyTown;
    }
}
