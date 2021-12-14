package persistant;

import services.SqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Genealogy {

    /**
     *will add the person to the database
     * @param name : name of the person
     * @return : will return the personIdentity object of the person
     * @throws Exception : will throw Exception if name is null or empty
     */
    public PersonIdentity addPerson(String name) throws Exception {
        if(name == null || name.isEmpty()){
            throw new Exception("Invalid name");
        }
        PersonIdentity person = new PersonIdentity();
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Statement statement = connect.createStatement();
        String addPersonQuery = "INSERT INTO personidentity (personName) values ('"+name+"');";
        statement.execute(addPersonQuery);
        person.setPersonId(findPerson(name).getPersonId());
        person.setName(name);
        statement.close();
        connect.close();
        return person;
    }

    /**
     * will record the attributes of the person in to the database
     * @param person : the personIdentity of the person where attributes will be store in to the database
     * @param attributes : HashMap of the attributes with key and value pair
     * @return : will return the true if attributes are stored else false
     * @throws Exception : will throw Exception if person is null ,or it is not found into the database
     */
    public Boolean recordAttributes(PersonIdentity person,Map<String, String> attributes)
            throws Exception {
        if(person == null){
            throw new Exception("Invalid Person");
        }
        if(attributes == null || attributes.isEmpty()){
            throw new Exception("Invalid Attributes");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        int id = findPerson(person.getName()).getPersonId();

        /**
         * will go through every attribute and check whether it is in the database if not then it will store
         * the attributes into the database
         */
        for(String attribute:attributes.keySet()){
            boolean isFound = false;
            StringBuilder attributeKey = new StringBuilder(attribute.toLowerCase());
            if(attribute.toLowerCase().equals("date of birth") || attribute.toLowerCase().equals("dob")){
                attributeKey = new StringBuilder("dateOfBirth");
            } else if(attribute.toLowerCase().equals("location of birth") ||
                    attribute.toLowerCase().equals("birth location")){
                attributeKey = new StringBuilder("locationOfBirth");
            } else if(attribute.toLowerCase().equals("date of death") ||
                    attribute.toLowerCase().equals("dod")){
                attributeKey = new StringBuilder("dateOfDeath");
            }
            String searchKeyQuery = "SELECT * FROM attributes WHERE attributeKey = \""+
                    attributeKey.toString()+"\" and personId ="+id;
            Statement stmtSearchKey = connect.createStatement();
            ResultSet rsSearchQuery = stmtSearchKey.executeQuery(searchKeyQuery);
            /**
             * will update the row if the key of the attribute is already there but the value is different
             */
            while (rsSearchQuery.next()){
                 if(!attributes.get(attribute).equals(rsSearchQuery.getString("attributeValue"))){
                     String updateKeyQuery = "UPDATE attributes SET personId = "+id +", attributeKey = \""+
                             attributeKey.toString()+"\", attributeValue = \""+attributes.get(attribute)+"\"" +
                             "where attributeKey = \""+attributeKey+"\"";
                     Statement stmtUpdateKey = connect.createStatement();
                     stmtSearchKey.execute(updateKeyQuery);
                 }
                isFound = true;
                break;
            }
            if(isFound){
                continue;
            }
            String addAttributeQuery = "INSERT INTO attributes (personId,attributeKey,attributeValue) " +
                    "values("+id+",\""+attributeKey+"\",\""+attributes.get(attribute)+"\");";
            Statement stmtAddAttribute = connect.createStatement();
            stmtAddAttribute.execute(addAttributeQuery);
            stmtAddAttribute.close();
            stmtSearchKey.close();
        }

        connect.close();
     return true;
    }

    /**
     * will record the reference the people to the database
     * @param person : the personIdentity object for the person
     * @param reference : reference for the person to store
     * @return : will return true if the reference is stored into the database
     * @throws Exception : will throw Exception if person is null or person is not found in family tree
     */
    public Boolean recordReference( PersonIdentity person, String reference )
            throws Exception {
        if(person == null){
            throw new Exception("Invalid person");
        }
        if(reference.isEmpty()){
            throw new Exception("Invalid reference");
        }

        int id = findPerson(person.getName()).getPersonId();

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Statement statement = connect.createStatement();

        String searchReferenceQuery = "SELECT *  FROM sourcereference where sourceRef = \""+reference+
                "\" and personId ="+ id;
        Statement stmtSearchReference = connect.createStatement();
        ResultSet rsReference = stmtSearchReference.executeQuery(searchReferenceQuery);
        while(rsReference.next()) {
            return false;
        }

        String recordQuery = "INSERT INTO sourcereference values ("+id+",\""+reference+"\");";
        statement.execute(recordQuery);
        return true;
    }

    /**
     will record the notes for the people to the database
     * @param person : the personIdentity object for the person
     * @param note : note for the person to store
     * @return : will return true if the note is stored into the database
     * @throws Exception : will throw Exception if person is null or person is not found in family tree
     **/
    public Boolean recordNote( PersonIdentity person, String note)
            throws Exception {
        if(person == null){
            throw new Exception("Invalid person");
        }
        if(note.isEmpty()){
            throw new Exception("Invalid reference");
        }

        int id = findPerson(person.getName()).getPersonId();

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Statement statement = connect.createStatement();

        String searchNoteQuery = "SELECT *  FROM notes where note = \""+note+
                "\" and personId ="+ id;
        Statement stmtSearchNote = connect.createStatement();
        ResultSet rsNote = stmtSearchNote.executeQuery(searchNoteQuery);
        if(rsNote.next()) {
            return false;
        }

        String recordQuery = "INSERT INTO notes values ("+id+",\""+note+"\");";
        statement.execute(recordQuery);
        return true;
    }

    /**
     * will find person by its name
     * @param name : name of the person
     * @return : PersonIdentity object of the person
     * @throws Exception : will throw Exception if name is null or empty, or person is not found in database
     */
    public PersonIdentity findPerson( String name ) throws Exception {
        if(name == null || name.isEmpty()){
            throw new Exception("name is empty/null");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Statement statement = connect.createStatement();

        String findIdQuery = "SELECT * FROM personIdentity where personName = \""+name+"\";";
        ResultSet rsId = statement.executeQuery(findIdQuery);
        PersonIdentity person = new PersonIdentity();
        if(rsId.next()) {
            person.setName(rsId.getString("personName"));
            person.setPersonId(rsId.getInt("personId"));
        }else{
            throw new Exception("person not found");
        }
        rsId.close();
        statement.close();
        connect.close();
        return person;
    }

    /**
     * will find the person based on personIdentity object
     * @param id : personIdentity object to search for
     * @return : will return the name of the person if its in the database
     * @throws Exception : will throw Exception if id is null or person is not found
     */
    public String findName(PersonIdentity id) throws Exception {
        if(id == null){
            throw new Exception("id can not be null");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Statement statement = connect.createStatement();
        ResultSet resultSet;

        String  findNameQuery = "select personId,personName from personIdentity where personName =\""
                +id.getName()+"\";";
        resultSet = statement.executeQuery(findNameQuery);
        StringBuilder personName = new StringBuilder("");
        if(resultSet.next()){
            personName.append(resultSet.getString("personName"));
        }else {
            throw new Exception("Person does not found");
        }
        resultSet.close();
        statement.close();
        connect.close();
        return personName.toString();
    }

    /**
     * will track the biological relation between two person in the group
     * @param person1 : personIdentity object for person 1
     * @param person2 : personIdentity object for person 2
     * @return : will return the biological relation of two person 1 and person 2
     * @throws Exception : will throw an Exception if person is null or it is not found in the family media
     */
    public BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 )
            throws Exception {
        if(person1 == null || person2 == null){
            throw new Exception("person is null");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        try {
            findPerson(person1.getName()).getPersonId();
            findPerson(person2.getName()).getPersonId();
        }catch (Exception e){
            throw new Exception("person does not found");
        }

        int person1Level = Integer.MAX_VALUE;
        int person2Level = Integer.MAX_VALUE;
        String degreeOfCousinship = "None";
        String degreeOfRemoval = "None";

        String person1Query = "SELECT * FROM biologicalparentingrelation where childId = " + person1.getPersonId();
        Statement stmtPerson1 = connect.createStatement();
        ResultSet rsPerson1 = stmtPerson1.executeQuery(person1Query);
        HashMap<Integer, Integer> person1List = new HashMap<>();
        HashMap<Integer, Integer> person2List = new HashMap<>();

        while (rsPerson1.next()) {
            person1List.put(rsPerson1.getInt("ancestorId"), rsPerson1.getInt("generation"));
        }
        rsPerson1.close();
        stmtPerson1.close();

        String person2Query = "SELECT * FROM biologicalparentingrelation where childId = " + person2.getPersonId();
        Statement stmtPerson2 = connect.createStatement();
        ResultSet rsPerson2 = stmtPerson2.executeQuery(person2Query);

        while (rsPerson2.next()) {
            person2List.put(rsPerson2.getInt("ancestorId"), rsPerson2.getInt("generation"));
        }

        /**
         * find if the person 1 is ancestor or person 2 is ancestor
         * ,or it will check the generations between two persons and
         * count the degree of cousinship and degree of removal
        */

        if (person2List.containsKey(person1.getPersonId())) {
            person1Level = 0;
            person2Level = person2List.get(person1.getPersonId());
        } else if (person1List.containsKey(person2.getPersonId())) {
            person1Level = 0;
            person2Level = person1List.get(person2.getPersonId());
        } else {
            boolean isfound = false;
            for (int person1Key : person1List.keySet()) {
                for (int person2Key : person2List.keySet()) {
                    if (person1Key == person2Key) {
                        person1Level = person1List.get(person1Key);
                        person2Level = person2List.get(person2Key);
                        isfound = true;
                        break;
                    }
                }
                if(isfound){
                    break;
                }
            }
        }
        if(person1Level != Integer.MAX_VALUE) {
            degreeOfCousinship = String.valueOf(Math.min(person1Level, person2Level) - 1);
            degreeOfRemoval = String.valueOf(Math.abs(person2Level - person1Level));
        }else {
            degreeOfCousinship = "None";
            degreeOfRemoval = "None";
        }
        BiologicalRelation relation = new BiologicalRelation();
        relation.setChildId(person1.getPersonId());
        relation.setAncestorId(person2.getPersonId());
        relation.setCousinship(degreeOfCousinship);
        relation.setRemoval(degreeOfRemoval);

        return relation;
    }

    /**
     * it will find the notes and references for particular person from the database
     * @param person : personIdentity object for person
     * @return : will return notes and references for given person object
     * @throws Exception : will throw Exception if person is null or person is not found
     */

    public List<String> notesAndReferences(PersonIdentity person ) throws Exception {
        if(person == null){
            throw new Exception("Person can not be null");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        try {
            findPerson(person.getName());
        }catch (Exception e){
            throw new Exception("person not found");
        }
        List<String> list = new ArrayList<>();

        String notesQuery = "SELECT * FROM notes where personID = " + person.getPersonId();
        Statement stmtNotes = connect.createStatement();
        ResultSet rsNotes = stmtNotes.executeQuery(notesQuery);
        while (rsNotes.next()){
            list.add(rsNotes.getString("note"));
        }

        String referenceQuery = "SELECT * FROM sourceReference WHERE personID = " + person.getPersonId();
        Statement stmtReference = connect.createStatement();
        ResultSet rsReference = stmtReference.executeQuery(referenceQuery);
        while (rsReference.next()){
            list.add(rsReference.getString("sourceRef"));
        }
        return list;
    }
}