package persistant;

import services.PersonService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}