package persistant;

import services.SqlConnection;

import java.sql.*;
import java.util.*;

/**
 * @author Keyur Vaghani (B00901000)
 * MediaArchive class will operate the media file in the database
 */

public class MediaArchive {

    /**
     * Will add the file to the database and return object of the FileIdentifier
     * @param fileLocation : String value for fileLocation or name
     * @return : return the object of fileIdentifier
     * @throws Exception: if fileLocation is null or empty then throws Exception
     */
    public FileIdentifier addMediaFile( String fileLocation ) throws Exception {
        if(fileLocation == null || fileLocation.isEmpty()){
            throw new Exception("file can not be null or empty");
        }

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        FileIdentifier file = new FileIdentifier();

        /**
         * will create the object of the MediaArchive object and search if the media file already exists into the
         * database if it exists then throws the exception otherwise will add the file to the table named
         * fileIdentifier
         */
        MediaArchive mediaArchive = new MediaArchive();
        try {
            mediaArchive.findMediaFile(fileLocation);
        }catch (Exception e){
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


    /**
     * will record the attributes passed by the Hash map such as date, location, tag and individuals in media etc.
     * @param fileIdentifier : the media file object of which storing the attributes
     * @param attributes : the Hashmap of the attributes with key and value pairs
     * @return : will return true if the attribute is stored into the database otherwise false
     * @throws Exception : will throw the exception if fileIdentifier or attributes is null or attributes is empty
     */
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
        } catch (Exception e) {
            return false;
        }

        /**
         * will iterate through every attribute in the attributes HashMap and check wheather if its already
         * into the data then insert new attributes to the database
         */
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

    /**
     * will store the PersonIdentity given in the List in the database
     * @param fileIdentifier : FileIdentifier class object for which the personIdentity object will be stored in the
     *                       database
     * @param people : the HashMap for the PersonIdentity class object
     * @return : will return true if the all personIdentity objects stored into the database
     * @throws Exception : will throw Exception if fileIdentifies or people, or PersonIdentity does not exist in the
     *                     Database
     */
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

        /**
         * will iterate through every person from the HashMap and then insert every personIdentity into the database
         */
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

    /**
     * will record the tags for the media into the database
     * @param fileIdentifier :
     * @param tags :
     * @return
     * @throws Exception
     */
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

    /**
     * will find the media file from the media archive
     * @param fileIdentifier : is the fileIdentifier object to identify the file from media archive
     * @return : will return file name if file exists.
     * @throws Exception : will throw an exception if fileIdentifier is null or file does not exist
     * in the media archive
     */
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
            rsFileId.getString("fileId");
        }else{
            throw new Exception("Media file not found");
        }
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return fileIdentifier.fileName;
    }

    /**
     * it will find the media file from media archive
     * @param name : name of th media file
     * @return : will return if media file is exists in the media archive
     * @throws Exception : will return Exception if name is empty or null or file does not exist
     */
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
            throw new Exception("Media File not found");
        }
        rsFileId.close();
        stmtFileId.close();
        connect.close();
        return file;
    }

    /**
     * will return the set of the fileIdentifiers filtered by tags between starting date and ending date
     * @param tag : tag for the file that needs to be searched
     * @param startDate : staring date to search for
     * @param endDate : ending date to search for
     * @return : will return set of fileIdentifiers
     * @throws Exception : will throw an Exception if tag is null or startDate or/and endDate has invalid format
     */
    public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate)
            throws Exception {
        if(tag == null || tag.isEmpty()){
            throw new Exception("tag is empty");
        }

        /**
         * regular expression for the validating dates for formatting "YYYY-MM-DD"
         */
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

        /**
         * Query string if both date are valid
         */
        String findTagBothValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "between \""+startDate+"\" and \""+endDate+"\") group by tags.fileId;";

        /**
         * Query string if start date is valid but end date is null
         */
        String findTagStartValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                ">= \""+startDate+"\") group by tags.fileId;";

        /**
         * Query string if end date is valid but start date is null
         */
        String findTagEndValid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "<= \""+endDate+"\") group by tags.fileId;";

        /**
         * Query string is start date and end date are null
         */
        String findTagBothInvalid = "SELECT * FROM fileIdentifier left join mediaAttributes \n" +
                "on fileIdentifier.fileId = mediaAttributes.fileId left join tags on tags.fileId = " +
                "fileIdentifier.fileId where (mediaAttributes.mediaAttributeKey = \"date\" or " +
                "mediaAttributes.mediaAttributeKey is null) and tags.tag = \""+tag+
                "\" and (DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "is null) group by tags.fileId;";

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

        /**
         * will store the file for the specific tag into the list
         */
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

    /**
     * will find the media file by the location between starting date and ending date
     * @param location : location of the file where it is taken
     * @param startDate : starting date to search for
     * @param endDate : ending date to search for
     * @return : set of the fileIdentifier object that has been filtered
     * @throws Exception : will throw Exception if location is null or startDate and/or endDate is invalid
     */
    public Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate)
            throws Exception {
        if(location == null || location.isEmpty()){
            throw new Exception("location is empty");
        }

        /**
         * regular expression validating dates
         */
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

        /**
         * Query if both dates are valid
         */
        String findTagBothValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "between \""+startDate+"\" and \""+endDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        /**
         * Query if starting date valid but ending date is null
         */
        String findTagStartValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                ">= \""+startDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        /**
         * Query if ending date is valid but starting date is null
         */
        String findTagEndValid = "SELECT * FROM (SELECT fileIdentifier.fileId,fileIdentifier.fileName " +
                "FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId " +
                "where  (mediaAttributes.mediaAttributeKey = \"location\" and  mediaAttributes.mediaAttributeValue = " +
                "\""+location+"\")) as X join (SELECT mediaAttributes.* FROM fileIdentifier \n" +
                "left join mediaAttributes on fileIdentifier.fileId = mediaAttributes.fileId \n" +
                "where(mediaAttributes.mediaAttributeKey = \"date\" and " +
                "DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "<= \""+endDate+"\")) as Y ON X.fileId = Y.fileId group by X.fileId;";

        /**
         * Query if both dates are null
         */
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

    /**
     * will find the individuals who are in the media
     * @param people : the list of the PersonIdentity
     * @param startDate : starting date to search for
     * @param endDate : ending date of search for
     * @return : will return the list of fileIdentifier
     * @throws Exception :will throw exception if people is null or personIdentity is not found in to the family
     * relation tree
     */
    public List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String
            endDate) throws Exception {
        if(people == null || people.isEmpty()){
            throw new Exception("people can not be null");
        }

        /**
         * regular expression for date validation
         */
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

        Genealogy genealogy = new Genealogy();
        StringBuilder queryAttached = new StringBuilder();
        int i = 0;
        for(PersonIdentity person:people){
            int personId = genealogy.findPerson(person.getName()).getPersonId();
            if(i!=0){
                queryAttached.append(" or ");
            }
            queryAttached.append("personId = ").append(personId);
            i++;
        }

        /**
         * Query if both dates are valid
         */
        String findTagBothValid = "SELECT * FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = " +
                "mediaAttributes.fileId left join individuals on individuals.fileId = fileIdentifier.fileId where " +
                "(mediaAttributes.mediaAttributeKey = \"date\" or mediaAttributes.mediaAttributeKey is null) and " +
                "("+queryAttached+")and \n" +
                "(DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) is null \n" +
                "or DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "between \""+startDate+"\" and \""+endDate+"\") group by fileIdentifier.fileId order by " +
                "mediaAttributes.mediaAttributeValue,fileName;";

        /**
         * Query if start date is valid but end date is null
         */
        String findTagStartValid = "SELECT * FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = " +
                "mediaAttributes.fileId left join individuals on individuals.fileId = fileIdentifier.fileId where " +
                "(mediaAttributes.mediaAttributeKey = \"date\" or mediaAttributes.mediaAttributeKey is null) and " +
                "("+queryAttached+")and \n" +
                "(DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) is null \n" +
                "or DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                ">= \""+startDate+"\") group by fileIdentifier.fileId order by " +
                "mediaAttributes.mediaAttributeValue,fileName;";

        /**
         * Query if end date is valid but start date is null
         */
        String findTagEndValid = "SELECT * FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = " +
                "mediaAttributes.fileId left join individuals on individuals.fileId = fileIdentifier.fileId where " +
                "(mediaAttributes.mediaAttributeKey = \"date\" or mediaAttributes.mediaAttributeKey is null) and " +
                "("+queryAttached+")and \n" +
                "(DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) is null \n" +
                "or DATE(STR_TO_DATE(mediaAttributes.mediaAttributeValue,\"%Y-%m-%d\")) \n" +
                "<= \""+endDate+"\") group by fileIdentifier.fileId order by " +
                "mediaAttributes.mediaAttributeValue,fileName;";

        /**
         * Query if both dates are invalid
         */
        String findTagBothInvalid = "SELECT * FROM fileIdentifier left join mediaAttributes on fileIdentifier.fileId = " +
                "mediaAttributes.fileId left join individuals on individuals.fileId = fileIdentifier.fileId where " +
                "(mediaAttributes.mediaAttributeKey = \"date\" or mediaAttributes.mediaAttributeKey is null) and " +
                "("+queryAttached+")and group by fileIdentifier.fileId order by " +
                "mediaAttributes.mediaAttributeValue,fileName;";

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

        SqlConnection sqlConnection = new SqlConnection();
        Connection connect = sqlConnection.setConnection();
        List<FileIdentifier> list = new ArrayList<>();
        Statement stmtLocation = connect.createStatement();
        ResultSet rsLocation = stmtLocation.executeQuery(finalQuery);
        while(rsLocation.next()){
            FileIdentifier file = new FileIdentifier();
            file.setFileName(rsLocation.getString("fileIdentifier.fileName"));
            file.setFileId(rsLocation.getInt("fileIdentifier.fileId"));
            list.add(file);
        }
        return list;

    }

    /**
     * will return fileIdentifiers for the immediate children
     * @param person : the personIdentity object of the parent
     * @return : will return the list of the fileIdentifier objects
     * @throws Exception : will throw Exception if person is null or it is not in the family tree
     */
    public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person)
            throws Exception {
        if(person == null){
            throw new Exception("person is null");
        }
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