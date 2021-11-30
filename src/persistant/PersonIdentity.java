package persistant;
import services.PersonService;
import java.sql.*;

public class PersonIdentity {
    private String name;
    private String dateOfBirth;
    private String birthLocation;
    private String dateOfDeath;
    private String deathLocation;
    private String gender;
    private String occupation;
    private String referenceMaterial;
    private String notes;

    public PersonIdentity(String name,String dateOfBirth,String birthLocation,
                          String dateOfDeath,String deathLocation,String gender,
                          String occupation,String referenceMaterial,String notes){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.birthLocation = birthLocation;
        this.dateOfDeath = dateOfDeath;
        this.deathLocation = deathLocation;
        this.gender = gender;
        this.occupation = occupation;
        this.referenceMaterial = referenceMaterial;
        this.notes = notes;
    }

    public PersonIdentity(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(String birthLocation) {
        this.birthLocation = birthLocation;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getDeathLocation() {
        return deathLocation;
    }

    public void setDeathLocation(String deathLocation) {
        this.deathLocation = deathLocation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getReferenceMaterial() {
        return referenceMaterial;
    }

    public void setReferenceMaterial(String referenceMaterial) {
        this.referenceMaterial = referenceMaterial;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PersonIdentity addPerson(String name) throws SQLException, ClassNotFoundException {
        PersonIdentity person = new PersonIdentity("");
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();
        statement.execute("INSERT INTO personidentity (personName) values " + "( '"+name+"');");
        
        statement.close();
        connect.close();
        return person;
    }
}