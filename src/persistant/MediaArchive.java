package persistant;

import services.PersonService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MediaArchive {
    public FileIdentifier addMediaFile( String fileLocation ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        FileIdentifier file = new FileIdentifier();
        file.fileName = fileLocation;

        String addFileQuery = "INSERT INTO fileIdentifier (fileName) values(\""+file.fileName+"\")";
        Statement stmtAddFile = connect.createStatement();
        stmtAddFile.execute(addFileQuery);
        stmtAddFile.close();

        connect.close();
        return file;
    }

    public Boolean recordMediaAttributes( FileIdentifier fileIdentifier, Map<String, String> attributes )
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        int fileId = findFileId(fileIdentifier);

        Statement stmtAddAttributes = connect.createStatement();
        for (String attribute : attributes.keySet()) {
            StringBuilder queryAttached = new StringBuilder();
            if(attribute.toLowerCase().equals("date") || attribute.toLowerCase().equals("date of creation")){
                queryAttached.append("fileDate = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE fileIdentifier SET " + queryAttached + " WHERE fileId = " + fileId;
                stmtAddAttributes.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("location")){
                queryAttached.append("location = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE fileIdentifier SET " + queryAttached + " WHERE fileId = " + fileId;
                stmtAddAttributes.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("province")){
                queryAttached.append("province = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE fileIdentifier SET " + queryAttached + " WHERE fileId = " + fileId;
                stmtAddAttributes.execute(recordQuery);
            }

            else if(attribute.toLowerCase().equals("country")){
                queryAttached.append("country = \"").append(attributes.get(attribute)).append("\"");
                String recordQuery = "UPDATE fileIdentifier SET " + queryAttached + " where fileId = " + fileId;
                stmtAddAttributes.execute(recordQuery);
            }
        }
        stmtAddAttributes.close();
        connect.close();
        return false;
    }

    public Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people )
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        int fileId = findFileId(fileIdentifier);
        Genealogy genealogy = new Genealogy();
        for(PersonIdentity person:people) {
            int personId = genealogy.findPersonId(person);
            String addPeople = "INSERT INTO individuals VALUES(" + fileId + ","+personId+");";
            Statement stmtAddPeople = connect.createStatement();
            stmtAddPeople.execute(addPeople);
            stmtAddPeople.close();
        }
        connect.close();
        return false;
    }

    public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate)
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Set<FileIdentifier> list = new HashSet<>();

        String findTag = "SELECT * from tags join fileIdentifier on tags.fileId = fileIdentifier.fileId \n" +
                "WHERE ((DATE(STR_TO_DATE(fileDate,\"%Y-%m\")) between \""+startDate+"\" and \""+endDate+"\") " +
                "or fileDate IS NULL) and tag = \""+tag+"\";";
        Statement stmtTags = connect.createStatement();
        ResultSet rsTags = stmtTags.executeQuery(findTag);
        while(rsTags.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileName(rsTags.getString("fileName"));
            list.add(file);
        }
        return list;
    }

    public Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate)
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        Set<FileIdentifier> list = new HashSet<>();

        String findLocation = "SELECT * from tags join fileIdentifier on tags.fileId = fileIdentifier.fileId \n" +
                "WHERE ((DATE(STR_TO_DATE(fileDate,\"%Y-%m\")) between \""+startDate+"\" and \""+endDate+"\") " +
                "or fileDate IS NULL) and location = \""+location.toLowerCase()+"\";";
        Statement stmtLocation = connect.createStatement();
        ResultSet rsLocation = stmtLocation.executeQuery(findLocation);
        while(rsLocation.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileName(rsLocation.getString("fileName"));
            list.add(file);
        }
        return list;
    }

    public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person)
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();
        List<FileIdentifier> list = new ArrayList<>();

        Genealogy genealogy = new Genealogy();
        int personId = genealogy.findPersonId(person);
        String findFamilyMediaQuery = "SELECT * FROM fileIdentifier join individuals on " +
                "fileIdentifier.fileId = individuals.fileId join ( SELECT * FROM biologicalRelation " +
                "where parentId = "+personId+" and level = 1 ) as X on individuals.personId = X.childId " +
                "group by fileIdentifier.fileId";
        Statement stmtFamilyMedia = connect.createStatement();
        ResultSet rsFamlilyMedia = stmtFamilyMedia.executeQuery(findFamilyMediaQuery);

        while(rsFamlilyMedia.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileName(rsFamlilyMedia.getString("fileIdentifier.fileName"));
            list.add(file);
        }
        return list;
    }

    public Boolean tagMedia( FileIdentifier fileIdentifier, String tags )
            throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        int fileid = findFileId(fileIdentifier);
        String addTag = "INSERT INTO tags (fileId,tag) VALUES("+fileid+",\""+tags+"\")";
        Statement stmtAddTag = connect.createStatement();
        stmtAddTag.execute(addTag);
        stmtAddTag.close();
        connect.close();
        return false;
    }

    public FileIdentifier findMediaFile( String name ) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        String findFileIdQuery = "SELECT * FROM fileIdentifier where fileName = \""+name+"\";";
        Statement stmtFileId = connect.createStatement();
        ResultSet rsFileId = stmtFileId.executeQuery(findFileIdQuery);
        rsFileId.next();
        FileIdentifier file = new FileIdentifier();
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return file;
    }

    public int findFileId (FileIdentifier fileIdentifier) throws SQLException, ClassNotFoundException {
        PersonService personService = new PersonService();
        Connection connect = personService.setConnection();

        String findFileIdQuery = "SELECT * FROM fileIdentifier where fileName = \""+fileIdentifier.getFileName()+"\";";
        Statement stmtFileId = connect.createStatement();
        ResultSet rsFileId = stmtFileId.executeQuery(findFileIdQuery);
        rsFileId.next();
        int id = Integer.parseInt(rsFileId.getString("fileId"));
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return id;
    }
}


