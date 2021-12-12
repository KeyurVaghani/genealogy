package persistant;

import services.PersonService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class BiologicalRelation {
    private int child_id;
    private int parent_id;
    private String childRelation;
    private int level;
    private String cousinship;
    private String removal;

    public int getChild_id() {return child_id;}

    public void setChild_id(int child_id) {this.child_id = child_id;}

    public int getParent_id() {return parent_id;}

    public void setParent_id(int parent_id) {this.parent_id = parent_id;}

    public String getChildRelation() {
        return childRelation;
    }

    public void setChildRelation(String childRelation) {
        this.childRelation = childRelation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCousinship() {return cousinship;}

    public void setCousinship(String cousinship) {this.cousinship = cousinship;}

    public String getRemoval() {return removal;}

    public void setRemoval(String removal) {this.removal = removal;}

    public Boolean recordChild(PersonIdentity parent, PersonIdentity child ) throws SQLException,
            ClassNotFoundException {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        PersonService service = new PersonService();
        Connection connect = service.setConnection();
        Genealogy genealogy = new Genealogy();
        //if both are in the table then
        int parentId = genealogy.findName(parent);
        int childId = genealogy.findName(child);
        String query = "INSERT INTO biologicalRelation values ("+childId+","+parentId+",\"parent\",1)";
        Statement stmtParent = connect.createStatement();
        stmtParent.executeUpdate(query);

        String findAncestorsQuery = "SELECT * FROM biologicalrelation where childId = "+parentId;
        Statement stmtAncestors = connect.createStatement();
        ResultSet rsAncestors = stmtAncestors.executeQuery(findAncestorsQuery);
        while(rsAncestors.next()){
            String insertRelationQuery = "INSERT INTO biologicalrelation values("+childId+","+
                    rsAncestors.getInt("parentId")+",\"parent\","+
                    (rsAncestors.getInt("level")+1) +")";
            Statement stmtParents = connect.createStatement();
            stmtParents.execute(insertRelationQuery);
            stmtParents.close();
        }

        String findDescendentsQuery = "SELECT * FROM biologicalRelation where parentId = "+ childId;
        Statement stmtDescendents = connect.createStatement();
        ResultSet rsDescendentsQuery = stmtDescendents.executeQuery(findDescendentsQuery);
        while (rsDescendentsQuery.next()){
            String insertRelationQuery = "INSERT INTO biologicalRelation values("+
                    rsDescendentsQuery.getString("childId")+","+ parentId+
                    ",\"parent\","+(rsDescendentsQuery.getInt("level")+1) +")";
            Statement stmtchilds = connect.createStatement();
            stmtchilds.execute(insertRelationQuery);
            stmtchilds.close();
        }

        stmtAncestors.close();
        stmtParent.close();
        connect.close();
        return false;
    }
}