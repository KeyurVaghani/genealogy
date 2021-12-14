package persistant;

import services.SqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class BiologicalRelation {
    private int childId;
    private int ancestorId;
    private int generation;
    private String cousinship;
    private String removal;

    public int getChildId() {return childId;}

    public void setChildId(int childId) {this.childId = childId;}

    public int getAncestorId() {return ancestorId;}

    public void setAncestorId(int ancestorId) {this.ancestorId = ancestorId;}

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public String getCousinship() {return cousinship;}

    public void setCousinship(String cousinship) {this.cousinship = cousinship;}

    public String getRemoval() {return removal;}

    public void setRemoval(String removal) {this.removal = removal;}

    public Boolean recordChild(PersonIdentity parent, PersonIdentity child ) throws Exception {
        if(parent == null || child == null){
            throw new Exception("Invalid parent/child");
        }
        if(parent.getPersonId() == child.getPersonId()){
            throw new Exception("Parent Child can not be same");
        }

        SqlConnection service = new SqlConnection();
        Connection connect = service.setConnection();
        Genealogy genealogy = new Genealogy();

        int ancestorId;
        int childId;
        try {
            ancestorId = genealogy.findPerson(parent.getName()).getPersonId();
            childId = genealogy.findPerson(child.getName()).getPersonId();
        }catch (Exception e){
            return false;
        }

        String searchRelation = "SELECT * FROM biologicalParentingRelation WHERE childID="+childId +
                " AND ancestorId = "+ancestorId +" AND generation =" + 1;
        Statement stmtSearchRelation = connect.createStatement();
        ResultSet rsRelation = stmtSearchRelation.executeQuery(searchRelation);
        if(rsRelation.next()){
            return false;
        }

        String query = "INSERT INTO biologicalParentingRelation values ("+childId+","+ancestorId+",1)";
        Statement stmtParent = connect.createStatement();
        stmtParent.executeUpdate(query);

        String findAncestorsQuery = "SELECT * FROM biologicalParentingRelation where childId = "+ancestorId;
        Statement stmtAncestors = connect.createStatement();
        ResultSet rsAncestors = stmtAncestors.executeQuery(findAncestorsQuery);
        while(rsAncestors.next()){
            String insertRelationQuery = "INSERT INTO biologicalParentingRelation values("+childId+","+
                    rsAncestors.getInt("ancestorId")+","+
                    (rsAncestors.getInt("generation")+1) +")";
            Statement stmtParents = connect.createStatement();
            stmtParents.execute(insertRelationQuery);
            stmtParents.close();
        }

        String findDescendentsQuery = "SELECT * FROM biologicalparentingrelation where ancestorId = "+ childId;
        Statement stmtDescendents = connect.createStatement();
        ResultSet rsDescendentsQuery = stmtDescendents.executeQuery(findDescendentsQuery);
        while (rsDescendentsQuery.next()){
            String insertRelationQuery = "INSERT INTO biologicalparentingrelation values("+
                    rsDescendentsQuery.getString("childId")+","+ ancestorId +
                    ","+(rsDescendentsQuery.getInt("generation")+1) +")";
            Statement stmtchilds = connect.createStatement();
            stmtchilds.execute(insertRelationQuery);
            stmtchilds.close();
        }

        stmtAncestors.close();
        stmtParent.close();
        connect.close();
        return true;
    }

    public Boolean recordPartnering( PersonIdentity partner1, PersonIdentity partner2 ) throws Exception {
        if(partner1 == null || partner2 == null){
            throw new Exception("Invalid partner1/partner2");
        }
        if(partner1.getPersonId() == partner2.getPersonId()){
            throw new Exception("Partner1 Partner2 can not be same");
        }
        SqlConnection service = new SqlConnection();
        Connection connect = service.setConnection();
        Genealogy genealogy = new Genealogy();

        int partner1Id = genealogy.findPerson(partner1.getName()).getPersonId();
        int partner2Id = genealogy.findPerson(partner2.getName()).getPersonId();

        String searchPartnerQuery = "SELECT * FROM biologicalPartneringRelation WHERE partner1Id = "+ partner1Id +
        " and partner2Id = " + partner2Id + " and relation = \"PARTNER\"";
        Statement stmtSearchPartner = connect.createStatement();
        ResultSet rsPartner = stmtSearchPartner.executeQuery(searchPartnerQuery);
        if(rsPartner.next()){
            return false;
        }
        rsPartner.close();
        stmtSearchPartner.close();

        String insertQuery = "INSERT INTO biologicalPartneringRelation values ("+
                partner1Id+","+partner2Id+",\"PARTNER\")";
        Statement stmtInsertPartner = connect.createStatement();
        stmtInsertPartner.executeUpdate(insertQuery);
        stmtInsertPartner.close();

        connect.close();
        return true;
    }

    public Boolean recordDissolute( PersonIdentity partner1, PersonIdentity partner2 ) throws Exception {
        if(partner1 == null || partner2 == null){
            throw new Exception("Invalid partner1/partner2");
        }
        if(partner1.getPersonId() == partner2.getPersonId()){
            throw new Exception("Partner1 Partner2 can not be same");
        }
        SqlConnection service = new SqlConnection();
        Connection connect = service.setConnection();
        Genealogy genealogy = new Genealogy();

        int partner1Id = genealogy.findPerson(partner1.getName()).getPersonId();
        int partner2Id = genealogy.findPerson(partner2.getName()).getPersonId();

        String searchPartnerQuery = "SELECT * FROM biologicalPartneringRelation WHERE partner1Id = "+ partner1Id +
                " and partner2Id = " + partner2Id;
        Statement stmtSearchPartner = connect.createStatement();
        ResultSet rsPartner = stmtSearchPartner.executeQuery(searchPartnerQuery);
        if(rsPartner.next()){
            if(rsPartner.getString("relation").equals("PARTNER")){
                String updateDissoluteQuery = "UPDATE biologicalPartneringRelation SET partner1Id = "+partner1Id+
                        ",partner2Id = "+partner2Id+ ", relation = \"DISSOLUTE\"";
                Statement stmtDissolute = connect.createStatement();
                stmtDissolute.execute(updateDissoluteQuery);
            }else{
                return false;
            }
        }
        rsPartner.close();
        stmtSearchPartner.close();
        connect.close();
        return true;
    }

    public Set<PersonIdentity> descendents(PersonIdentity person, Integer generations )
            throws Exception {
        if(person == null){
            throw new Exception("person can not be null");
        }
        if (generations<=0){
            throw new Exception("generations can not be negative");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Genealogy genealogy = new Genealogy();
        try{
            genealogy.findPerson(person.getName());
        }catch(Exception e){
            throw new Exception("person does not found");
        }

        Set<PersonIdentity> descendentsList = new HashSet<>();

        String descendentsQuery = "SELECT * FROM biologicalparentingrelation join personIdentity on " +
                "biologicalparentingrelation.childId = personIdentity.personId where ancestorId = "+
                person.getPersonId() +" and generation <= "+generations+" ;";
        Statement stmtDescendentsQuery = connect.createStatement();
        ResultSet rsDescendents = stmtDescendentsQuery.executeQuery(descendentsQuery);
        while (rsDescendents.next()){
            PersonIdentity descendent = new PersonIdentity();
            descendent.setPersonId(rsDescendents.getInt("personId"));
            descendent.setName(rsDescendents.getString("personName"));
            descendentsList.add(descendent);
        }
        return descendentsList;
    }

    public Set<PersonIdentity> ancestores(PersonIdentity person, Integer generations )
            throws Exception {
        if(person == null){
            throw new Exception("person can not be null");
        }
        if (generations<=0){
            throw new Exception("generations can not be negative");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Genealogy genealogy = new Genealogy();
        try{
            genealogy.findPerson(person.getName());
        }catch(Exception e){
            throw new Exception("person does not found");
        }

        Set<PersonIdentity> ancestorsList = new HashSet<>();

        String ancestorsQuery = "SELECT * FROM biologicalparentingrelation join personIdentity " +
                "on biologicalparentingrelation.ancestorId = personIdentity.personId where childId = "
                +person.getPersonId()+" and generation <="+generations + ";";
        Statement stmtAncestorsQuery = connect.createStatement();
        ResultSet rsAncestors = stmtAncestorsQuery.executeQuery(ancestorsQuery);
        while (rsAncestors.next()){
            PersonIdentity ancestor = new PersonIdentity();
            ancestor.setPersonId(rsAncestors.getInt("personId"));
            ancestor.setName(rsAncestors.getString("personName"));
            ancestorsList.add(ancestor);
        }
        return ancestorsList;
    }
}