package persistant;

import services.PersonService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Relation {
    enum relative{PARENT,MARRIAGE,DIVORCE,NA};

    private UUID child_id;
    private UUID ancestor_id;
    private relative childRelation;
    private int level;

    public UUID getChild_id() {
        return child_id;
    }

    public void setChild_id(UUID child_id) {
        this.child_id = child_id;
    }

    public UUID getAncestor_id() {
        return ancestor_id;
    }

    public void setAncestor_id(UUID ancestor_id) {
        this.ancestor_id = ancestor_id;
    }

    public relative getChildRelation() {
        return childRelation;
    }

    public void setChildRelation(relative childRelation) {
        this.childRelation = childRelation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Boolean recordChild( PersonIdentity parent, PersonIdentity child ) throws SQLException, ClassNotFoundException {
        Relation relation = new Relation();
        PersonService service = new PersonService();
        Connection connect = service.setConnection();
        Statement statement = connect.createStatement();
        Genealogy genealogy = new Genealogy();
        //if both are in the table then
        int parentId = genealogy.findName(parent);
        int childId = genealogy.findName(child);
        String query = "INSERT INTO biologicalRelation values ("+ childId+","+parentId+",'parent',1)";
             statement.executeUpdate(query);
        statement.close();
        connect.close();
        return false;
    }
}
