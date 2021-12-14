package persistant;

/**
 * @author Keyur Vaghani (B00901000)
 * PersonIdentity Class is to identify the person Uniquly from the family tree
 */
public class PersonIdentity {
    private int personId;
    private String name;

    /**
     * getter method for personId
     * @return : Id of the person
     */
    public int getPersonId() {return personId;}

    /**
     * Setter method for the personId
     * @param personId : get the personId as parameter
     */
    public void setPersonId(int personId) {this.personId = personId;}

    /**
     * getter method for the personName
     * @return : name of the person
     */
    public String getName() {return name;}

    /**
     * setter method for the personName
     * @param name : take the personName as the parameter
     */
    public void setName(String name) {this.name = name;}
}