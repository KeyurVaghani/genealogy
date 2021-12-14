package persistant;

import services.SqlConnection;

import java.sql.*;
import java.util.*;

public class MediaArchive {
    public FileIdentifier addMediaFile( String fileLocation ) throws Exception {
        if(fileLocation == null || fileLocation.isEmpty()){
            throw new Exception("file can not be null or empty");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        FileIdentifier file = new FileIdentifier();

        MediaArchive mediaArchive = new MediaArchive();
        try {
            mediaArchive.findMediaFile(fileLocation);
        }catch (NullPointerException e){
            String addFileQuery = "INSERT INTO fileIdentifier (fileName) values(\""+fileLocation+"\")";
            PreparedStatement stmtAddFile = connect.prepareStatement(addFileQuery,Statement.RETURN_GENERATED_KEYS);
            stmtAddFile.executeUpdate(addFileQuery);
            ResultSet resultSet = stmtAddFile.getGeneratedKeys();
            if(resultSet.next()){
                file.setFileId(resultSet.getInt(1));
                file.setFileName(fileLocation);
            }
            stmtAddFile.close();
        }
        connect.close();
        return file;
    }


    public Boolean recordMediaAttributes( FileIdentifier fileIdentifier, Map<String, String> attributes )
            throws Exception {
        if (fileIdentifier == null) {
            throw new Exception("file can not be null");
        }
        if (attributes == null || attributes.isEmpty()) {
            throw new Exception("Attributes can not be empty or null");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        int fileId;
        try {
            fileId = findMediaFile(fileIdentifier.fileName).getFileId();
        } catch (NullPointerException e) {
            return false;
        }

        for (String attribute : attributes.keySet()) {
            boolean isFound = false;
            StringBuilder attributeKey = new StringBuilder(attribute.toLowerCase());
            String searchKeyQuery = "SELECT * FROM mediaAttributes WHERE mediaAttributeKey = \"" +
                    attributeKey.toString() + "\" and fileId =" + fileId;
            Statement stmtSearchKey = connect.createStatement();
            ResultSet rsSearchQuery = stmtSearchKey.executeQuery(searchKeyQuery);
            while (rsSearchQuery.next()) {
                if (!attributes.get(attribute).equals(rsSearchQuery.getString("mediaAttributeValue"))) {
                    String updateKeyQuery = "UPDATE mediaAttributes SET fileId = " + fileId +
                            ", mediaAttributeKey = \"" + attributeKey.toString() + "\", mediaAttributeValue = \"" +
                            attributes.get(attribute) + "\"" + "where mediaAttributeKey = \"" + attributeKey + "\"";
                    Statement stmtUpdateKey = connect.createStatement();
                    stmtUpdateKey.execute(updateKeyQuery);
                }
                isFound = true;
                break;
            }
                if (isFound) {
                    continue;
                }
                String addAttributeQuery = "INSERT INTO mediaAttributes (fileId,mediaAttributeKey,mediaAttributeValue) "
                        + "values(" + fileId + ",\"" + attributeKey + "\",\"" + attributes.get(attribute) + "\");";
                Statement stmtAddAttribute = connect.createStatement();
                stmtAddAttribute.execute(addAttributeQuery);
                stmtAddAttribute.close();
                stmtSearchKey.close();
            }
        connect.close();
        return false;
    }

        public Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people )
                throws Exception {
        if(fileIdentifier == null){
            throw new Exception("file can not be null");
        }
        if(people.isEmpty() || people == null){
            throw new Exception("people can not be null or empty");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Genealogy genealogy = new Genealogy();

        for(PersonIdentity person:people){
            try{
                genealogy.findName(person);
            }catch (Exception e){
                return false;
            }
        }
        int fileId = fileIdentifier.getFileId();
        try {
           findMediaFile(fileIdentifier.fileName).getFileId();
        }catch (Exception e){
           throw new Exception("File does not found");
        }
        for(PersonIdentity person:people) {
            int personId = genealogy.findPerson(person.getName()).getPersonId();
            String addPeople = "INSERT INTO individuals VALUES(" + fileId + ","+personId+");";
            Statement stmtAddPeople = connect.createStatement();
            stmtAddPeople.execute(addPeople);
            stmtAddPeople.close();
        }
        connect.close();
        return true;
    }

    public Boolean tagMedia( FileIdentifier fileIdentifier, String tags )
            throws Exception {
        if(tags == null || tags.isEmpty()){
            throw new Exception("tag is empty");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        try {
            findMediaFile(fileIdentifier);
        }catch (Exception e){
            return false;
        }

        String findTagQuery = "SELECT * FROM tags WHERE fileId = "+fileIdentifier.getFileId()+" and tag = \""+tags
                +"\"";
        Statement stmtFindTag = connect.createStatement();
        ResultSet rsTags = stmtFindTag.executeQuery(findTagQuery);
        if(rsTags.next()){
            return false;
        }

        String addTag = "INSERT INTO tags (fileId,tag) VALUES("+fileIdentifier.getFileId()+",\""+tags+"\")";
        Statement stmtAddTag = connect.createStatement();
        stmtAddTag.execute(addTag);
        stmtAddTag.close();
        connect.close();
        return true;
    }

    public String findMediaFile(FileIdentifier fileIdentifier) throws Exception {
        if(fileIdentifier == null ){
            throw new Exception("FileIdentifier can not be null");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        String findFileIdQuery = "SELECT * FROM fileIdentifier where fileName = \""+fileIdentifier.getFileName()+"\";";
        Statement stmtFileId = connect.createStatement();
        ResultSet rsFileId = stmtFileId.executeQuery(findFileIdQuery);
        if(rsFileId.next()) {
            Integer.parseInt(rsFileId.getString("fileId"));
        }else{
            throw new Exception("Media file not found");
        }
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return fileIdentifier.fileName;
    }

    public FileIdentifier findMediaFile( String name ) throws Exception {
        if(name == null || name.isEmpty()){
            throw new Exception("name is empty or null");
        }
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();

        String findFileIdQuery = "SELECT * FROM fileIdentifier where fileName = \""+name+"\";";
        Statement stmtFileId = connect.createStatement();
        ResultSet rsFileId = stmtFileId.executeQuery(findFileIdQuery);
        FileIdentifier file = new FileIdentifier();
        if(rsFileId.next()){
            file.setFileId(rsFileId.getInt("fileId"));
            file.setFileName(name);
        }else{
            throw new NullPointerException("Media File not found");
        }
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return file;
    }

    public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate)
            throws Exception {
        if(tag == null || tag.isEmpty()){
            throw new Exception("tag is empty");
        }

        String dateRegex = "[0-9]{4}-(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])";
        if(startDate != null) {
            boolean isStartValid = startDate.matches(dateRegex);
            if(!isStartValid){
                throw new Exception("Invalid date format. it should be \"YYYY-MM-DD\"");
            }
        }
        if(endDate != null) {
            boolean isEndValid = endDate.matches(dateRegex);
            if(!isEndValid){
                throw new Exception("Invalid date format. it should be \"YYYY-MM-DD\"");
            }
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Set<FileIdentifier> list = new HashSet<>();

        String findTagBothValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "between \""+startDate+"\" and \""+endDate+"\" group by tags.fileId);";

        String findTagStartValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                ">= \""+startDate+"\" group by tags.fileId);";

        String findTagEndValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "<= \""+endDate+"\" group by tags.fileId);";

        String findTagBothInvalid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "is null group by tags.fileId);";

        String finalQuery;
        if(startDate == null && endDate == null){
            finalQuery = findTagBothInvalid;
        }else if(endDate == null){
            finalQuery = findTagStartValid;
        }else if(startDate == null){
            finalQuery = findTagEndValid;
        }else {
            finalQuery = findTagBothValid;
        }

        Statement stmtTags = connect.createStatement();
        ResultSet rsTags = stmtTags.executeQuery(finalQuery);
        while(rsTags.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileId(rsTags.getInt("fileId"));
            file.setFileName(rsTags.getString("fileName"));
            list.add(file);
        }
        return list;
    }

    public Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate)
            throws Exception {
        if(location == null || location.isEmpty()){
            throw new Exception("location is empty");
        }

        String dateRegex = "[0-9]{4}-(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])";
        if(startDate != null) {
            boolean isStartValid = startDate.matches(dateRegex);
            if(!isStartValid){
                throw new Exception("Invalid date format. it should be \"YYYY-MM-DD\"");
            }
        }
        if(endDate != null) {
            boolean isEndValid = endDate.matches(dateRegex);
            if(!isEndValid){
                throw new Exception("Invalid date format. it should be \"YYYY-MM-DD\"");
            }
        }

        String findTagBothValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "between \""+startDate+"\" and \""+endDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        String findTagStartValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                ">= \""+startDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        String findTagEndValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "<= \""+endDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        String findTagBothInvalid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue " +
                " = \""+location+"\")) as X left join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" )) as Y ON X.fileId = Y.fileId group by X.fileId;";

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        Set<FileIdentifier> list = new HashSet<>();


        String finalQuery;
        if(startDate == null && endDate == null){
            finalQuery = findTagBothInvalid;
        }else if(endDate == null){
            finalQuery = findTagStartValid;
        }else if(startDate == null){
            finalQuery = findTagEndValid;
        }else {
            finalQuery = findTagBothValid;
        }

        Statement stmtLocation = connect.createStatement();
        ResultSet rsLocation = stmtLocation.executeQuery(finalQuery);
        while(rsLocation.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileName(rsLocation.getString("X.fileName"));
            file.setFileId(rsLocation.getInt("X.fileId"));
            list.add(file);
        }
        return list;
    }

    public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person)
            throws Exception {
        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        List<FileIdentifier> list = new ArrayList<>();

        Genealogy genealogy = new Genealogy();
        int ancestorId = genealogy.findPerson(person.getName()).getPersonId();
        String findFamilyMediaQuery = "SELECT * FROM fileIdentifier join individuals on " +
                "fileIdentifier.fileId = individuals.fileId join ( SELECT * FROM biologicalparentingrelation " +
                "where ancestorId = "+ancestorId+" and generation = 1 ) as X on individuals.personId = X.childId " +
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
}


