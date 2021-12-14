import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import persistant.FileIdentifier;
import persistant.Genealogy;
import persistant.MediaArchive;
import persistant.PersonIdentity;

import java.util.*;

public class testMediaArchive {

    @Test
    @Order(1)
    public void testAddFile() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.addMediaFile("txt file");
    }

    @Test
    @Order(2)
    public void testRecordMediaAttributes() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        FileIdentifier file = mediaArchive.findMediaFile("txt file");
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("date","2005-01-01");
        attributes.put("location","Toronto");
        attributes.put("province","Montreal");
        attributes.put("country","Canada");
        mediaArchive.recordMediaAttributes(file,attributes);
    }

    @Test
    @Order(3)
    public void testPeopleInMedia() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        FileIdentifier file = mediaArchive.addMediaFile("this is 3rd file");
        List<PersonIdentity> people = new ArrayList<>();
        people.add(new PersonIdentity());
        people.add(new PersonIdentity());
        people.add(new PersonIdentity());
        people.add(new PersonIdentity());
        mediaArchive.peopleInMedia(file,people);
    }

    @Test
    public void testTag() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        FileIdentifier file = mediaArchive.findMediaFile("txt file");
        String tag = "keyur";
        mediaArchive.tagMedia(file,tag);
    }

    @Test
    public void testFindMediaFile() {
            Assertions.assertThrows(Exception.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    MediaArchive mediaArchive = new MediaArchive();
                    mediaArchive.findMediaFile("this is file 2");
                }
            });
    }

    @Test
    public void testFindTagWhenBothDatesAreNull() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("keyur",null,null);
    }

    @Test
    public void testFindTagWhenStartDateIsNull() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("keyur",null,"2020-12-12");
    }

    @Test
    public void testFindTagWhenEndDatesIsNull() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("keyur","2020-10-12",null);
    }
    @Test
    public void testFindTagWithNonNull() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("keyur","2020-10-12","2020-12-12");
    }


    @Test
    public void testFindLocation() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByLocation("Toronto",null,null);
    }

    @Test
    public  void testIndividual() throws Exception{
        MediaArchive mediaArchive = new MediaArchive();
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.findPerson("A");
        PersonIdentity G = genealogy.findPerson("G");
        PersonIdentity J = genealogy.findPerson("J");
        Set<PersonIdentity> list = new HashSet<>();
        list.add(A);
        list.add(G);
        list.add(J);
        mediaArchive.findIndividualsMedia(list,"2001-05-05","2010-01-01");

    }

    @Test
    public void testFindBiologicalFamilyMedia() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        Genealogy genealogy  = new Genealogy();
        PersonIdentity person = genealogy.findPerson("D");
        mediaArchive.findBiologicalFamilyMedia(person);
    }
}
