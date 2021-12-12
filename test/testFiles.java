import org.junit.jupiter.api.*;
import persistant.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class testFiles {
    @Test
    public void addOnePerson() throws SQLException, ClassNotFoundException {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.addPerson("B");
    }

    @Test
    public void testRecordAttributes() throws SQLException, ClassNotFoundException {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = new PersonIdentity();
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("dob","2000-01-01");
        attributes.put("location of birth","halifax");
        attributes.put("gender","MALE");
        genealogy.recordAttributes(A,attributes);
    }

    @Test
    public void testReference() throws SQLException, ClassNotFoundException {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = new PersonIdentity();
        genealogy.recordReference(A,"this is word");
    }

    @Test
    public void testRecordChild() throws SQLException, ClassNotFoundException {
        PersonIdentity A = new PersonIdentity();
        PersonIdentity B = new PersonIdentity();
        PersonIdentity C = new PersonIdentity();
        PersonIdentity D = new PersonIdentity();
        PersonIdentity J = new PersonIdentity();
        PersonIdentity I = new PersonIdentity();
        PersonIdentity G = new PersonIdentity();
        PersonIdentity H = new PersonIdentity();
        PersonIdentity E = new PersonIdentity();
        PersonIdentity F = new PersonIdentity();
        PersonIdentity M = new PersonIdentity();
        PersonIdentity L = new PersonIdentity();
        PersonIdentity K = new PersonIdentity();

        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();
        genealogy.addPerson("A");
        genealogy.addPerson("B");
        genealogy.addPerson("C");
        genealogy.addPerson("D");
        genealogy.addPerson("J");
        genealogy.addPerson("I");
        genealogy.addPerson("G");
        genealogy.addPerson("H");
        genealogy.addPerson("E");
        genealogy.addPerson("F");
        genealogy.addPerson("M");
        genealogy.addPerson("L");
        genealogy.addPerson("K");

         biologicalRelation.recordChild(D,A);
        biologicalRelation.recordChild(D,B);
        biologicalRelation.recordChild(D,C);
        biologicalRelation.recordChild(J,D);
        biologicalRelation.recordChild(G,E);
        biologicalRelation.recordChild(H,F);
        biologicalRelation.recordChild(I,G);
        biologicalRelation.recordChild(I,H);
        biologicalRelation.recordChild(J,I);
        biologicalRelation.recordChild(M,I);
        biologicalRelation.recordChild(M,L);
        biologicalRelation.recordChild(L,K);

    }

    @Test
    public void testAddFile() throws SQLException, ClassNotFoundException {
        FileIdentifier file = new FileIdentifier();
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.addMediaFile("this is file location 1");
    }

    @Test
    public void testRecordMediaAttributes() throws SQLException, ClassNotFoundException {
        MediaArchive mediaArchive = new MediaArchive();
        FileIdentifier file = mediaArchive.addMediaFile("this is for location 2");
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("date","2000-01-01");
        attributes.put("location","halifax");
        attributes.put("province","Nova scotia");
        attributes.put("country","Canada");
        mediaArchive.recordMediaAttributes(file,attributes);
    }

    @Test
    public void testPeopleInMedia() throws SQLException, ClassNotFoundException {
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
    public void testTag() throws SQLException, ClassNotFoundException {
        MediaArchive mediaArchive = new MediaArchive();
        FileIdentifier file = mediaArchive.addMediaFile("this is for location 2");
        String tag = "geek";
        mediaArchive.tagMedia(file,tag);
    }

    @Test
    public void testRelation () throws SQLException, ClassNotFoundException {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person1 = genealogy.findPerson("J");
        PersonIdentity person2 = genealogy.findPerson("E");
        genealogy.findRelation(person1,person2);
    }

    @Test
    public void testFindTag() throws SQLException, ClassNotFoundException {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("garden","1998-01-01","2001-05-05");
    }

    @Test
    public void testFindLocation() throws SQLException, ClassNotFoundException {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByLocation("Halifax","1998-01-01","2001-05-05");
    }

    @Test
    public void testFindBiologicalFamilyMedia() throws SQLException, ClassNotFoundException {
        MediaArchive mediaArchive = new MediaArchive();
        Genealogy genealogy  = new Genealogy();
        PersonIdentity person = genealogy.findPerson("D");
        mediaArchive.findBiologicalFamilyMedia(person);
    }
}
