package persistant;

import services.PersonService;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Genealogy {
 public int findName(PersonIdentity id) throws SQLException, ClassNotFoundException {
     PersonService personService = new PersonService();
     Connection connect = personService.setConnection();
     Statement statement = connect.createStatement();
     ResultSet resultSet;
//     resultSet = statement.executeQuery("select personId,personName from personIdentity where personName ="+id.getName()+
//             "AND DOB ="+id.getDateOfBirth()+"AND birth_location ="+id.getBirthLocation() + "AND DOD =" +
//             id.getDateOfDeath() + "AND death_location =" + id.getDeathLocation() + "AND Gender =" +
//             id.getGender() +"AND occupation =" + id.getOccupation() +"AND reference_material" + id.getNotes() );
     resultSet = statement.executeQuery("select personId,personName from personIdentity where personName ='"
             +id.getName()+"';");
     int personId = -1;
     while (resultSet.next()){
         personId = Integer.parseInt(resultSet.getString("personId"));
     }
     resultSet.close();
     statement.close();
     connect.close();
     return personId;
 }

    public PersonIdentity addPerson(String name) throws SQLException, ClassNotFoundException {
        PersonIdentity person = new PersonIdentity();
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();
        String addPersonQuery = "INSERT INTO personidentity (personName) values ('"+person.getName()+"');";
        statement.execute(addPersonQuery);
        person.setName(name);
        statement.close();
        connect.close();

        return person;
    }

    public Boolean recordAttributes(PersonIdentity person,Map<String, String> attributes)
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();

        int id = findPersonId(person);

        for (String attribute : attributes.keySet()) {
            StringBuilder queryAttached = new StringBuilder();
            if(attribute.toLowerCase().equals("date of birth") || attribute.toLowerCase().equals("dob")){
                queryAttached.append("dateOfBirth = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("location of birth") ||
                    attribute.toLowerCase().equals("birth location")){
                queryAttached.append("locationOfBirth = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("date of death") || attribute.toLowerCase().equals("dod")){
                queryAttached.append("dateOfDeath = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("location of death") ||
                    attribute.toLowerCase().equals("deathlocation")){
                queryAttached.append("locationOfdeath = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }

            else if (attribute.toLowerCase().equals("gender")){
                String gender = "";
                if(attributes.get(attribute).toLowerCase().equals("male") ||
                attributes.get(attribute).toLowerCase().equals("m")){
                    gender = "MALE";
                }else if(attributes.get(attribute).toLowerCase().equals("female") ||
                        attributes.get(attribute).toLowerCase().equals("f")){
                    gender = "FEMALE";
                }
                queryAttached.append("gender = \"").append(gender).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("occupation")){
                queryAttached.append("occupation =").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE personIdentity set " + queryAttached + " where personId = " + id;
                statement.execute(recordQuery);
            }
        }

        statement.close();
        connect.close();
     return false;
    }

    public Boolean recordReference( PersonIdentity person, String reference ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();

        Genealogy genealogy = new Genealogy();
        int id = genealogy.findPersonId(person);

        String recordQuery = "INSERT INTO sourcereference values ("+id+",\""+reference+"\");";
        statement.execute(recordQuery);
        return false;
    }

    public Boolean recordChild( PersonIdentity parent, PersonIdentity child ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();


        return false;
    }

    public PersonIdentity findPerson( String name ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();

        String findIdQuery = "SELECT * FROM personIdentity where personName = \""+name+"\";";
        ResultSet rsId = statement.executeQuery(findIdQuery);
        rsId.next();
        PersonIdentity person = new PersonIdentity();
        person.setName(name);
        rsId.close();
        statement.close();
        connect.close();
        return person;
    }

    public BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 )
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        Genealogy genealogy = new Genealogy();
        int person1Id = genealogy.findPersonId(person1);
        int person2Id = genealogy.findPersonId(person2);
        int person1Level = Integer.MAX_VALUE;
        int person2Level = Integer.MAX_VALUE;
        String degreeOfCousinship = "None";
        String degreeOfRemoval = "None";

        String person1Query = "SELECT * FROM biologicalrelation where childId = " + person1Id;
        Statement stmtPerson1 = connect.createStatement();
        ResultSet rsPerson1 = stmtPerson1.executeQuery(person1Query);
        HashMap<Integer, Integer> person1List = new HashMap<>();
        HashMap<Integer, Integer> person2List = new HashMap<>();

        while (rsPerson1.next()) {
            person1List.put(rsPerson1.getInt("parentId"), rsPerson1.getInt("level"));
        }
        rsPerson1.close();
        stmtPerson1.close();

        String person2Query = "SELECT * FROM biologicalrelation where childId = " + person2Id;
        Statement stmtPerson2 = connect.createStatement();
        ResultSet rsPerson2 = stmtPerson2.executeQuery(person2Query);

        while (rsPerson2.next()) {
            person2List.put(rsPerson2.getInt("parentId"), rsPerson2.getInt("level"));
        }

        if (person2List.containsKey(person1Id)) {
            person1Level = 0;
            person2Level = person2List.get(person1Id);
        } else if (person1List.containsKey(person2Id)) {
            person1Level = 0;
            person2Level = person1List.get(person2Id);
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
        relation.setChild_id(person1Id);
        relation.setParent_id(person2Id);
        relation.setCousinship(degreeOfCousinship);
        relation.setRemoval(degreeOfRemoval);

        return relation;
    }

    public Set<PersonIdentity> descendents(PersonIdentity person, Integer generations )
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        int personId = findPersonId(person);
        Set<PersonIdentity> descendentsList = new HashSet<>();

        String descendentsQuery = "SELECT * FROM biologicalRelation where parentId = "+personId+" " +
                "and level <= "+generations+";";
        Statement stmtDescendentsQuery = connect.createStatement();
        ResultSet rsDescendents = stmtDescendentsQuery.executeQuery(descendentsQuery);
//        while (rsDescendents.next()){
//            PersonIdentity descendent = new PersonIdentity();
//            rsDescendents.
//
//        }
        return descendentsList;
    }

    public List<String> notesAndReferences(PersonIdentity person ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        int personId = findPersonId(person);
        List<String> list = new ArrayList<>();

        String notesQuery = "SELECT * FROM notes where personID = " + personId;
        Statement stmtNotes = connect.createStatement();
        ResultSet rsNotes = stmtNotes.executeQuery(notesQuery);
        while (rsNotes.next()){
            list.add(rsNotes.getString("note"));
        }

        String referenceQuery = "SELECT * FROM sourceReference WHERE personID = " + personId;
        Statement stmtReference = connect.createStatement();
        ResultSet rsReference = stmtReference.executeQuery(referenceQuery);
        while (rsReference.next()){
            list.add(rsReference.getString("sourceRef"));
        }

        return list;
    }

    public int findPersonId (PersonIdentity person) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Statement statement = connect.createStatement();

        String findIdQuery = "SELECT * FROM personIdentity where personName = \""+person.getName()+"\";";
        ResultSet rsId = statement.executeQuery(findIdQuery);
        rsId.next();
        int id = Integer.parseInt(rsId.getString("personId"));
        rsId.close();
        statement.close();
        connect.close();
        return id;
    }


}