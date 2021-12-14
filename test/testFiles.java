import org.junit.jupiter.api.*;
import persistant.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class testFiles {
    @Test
    public void addOnePerson() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.addPerson(null);
    }

    @Test
    public void testRecordAttributes() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.findPerson("G");
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("date of birth","1998-09-11");
        attributes.put("location of birth","halifax");
        attributes.put("gender","FEMALE");
        genealogy.recordAttributes(A,attributes);
    }

    @Test
    public void testReference() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.findPerson("A");
        genealogy.recordReference(A,"this is a word");
    }

    @Test
    public void testNote() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.findPerson("A");
        genealogy.recordNote(A,"note will be here");
    }

    @Test
    public void testRecordChild() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.addPerson("A");
        PersonIdentity B = genealogy.addPerson("B");
        PersonIdentity C = genealogy.addPerson("C");
        PersonIdentity D = genealogy.addPerson("D");
        PersonIdentity J = genealogy.addPerson("J");
        PersonIdentity I = genealogy.addPerson("I");
        PersonIdentity G = genealogy.addPerson("G");
        PersonIdentity H = genealogy.addPerson("H");
        PersonIdentity E = genealogy.addPerson("E");
        PersonIdentity F = genealogy.addPerson("F");
        PersonIdentity M = genealogy.addPerson("M");
        PersonIdentity L = genealogy.addPerson("L");
        PersonIdentity K = genealogy.addPerson("K");

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
    public void testRecordPartner() throws Exception{
        Genealogy genealogy = new Genealogy();
        BiologicalRelation relation = new BiologicalRelation();
        PersonIdentity partner1 = genealogy.findPerson("J");
        PersonIdentity partner2 = genealogy.findPerson("M");
        relation.recordPartnering(partner1,partner2);
    }

    @Test
    public void testRecordDissolute() throws Exception{
        Genealogy genealogy = new Genealogy();
        BiologicalRelation relation = new BiologicalRelation();
        PersonIdentity partner1 = genealogy.findPerson("J");
        PersonIdentity partner2 = genealogy.findPerson("M");
        relation.recordDissolute(partner1,partner2);
    }

    @Test
    public void testAddFile() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.addMediaFile("txt file");
    }

    @Test
    public void testFindMediaFile() throws  Exception{
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaFile("this is file 2");
    }

    @Test
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
        FileIdentifier file = mediaArchive.findMediaFile("this is file location 1");
        String tag = "keyur";
        mediaArchive.tagMedia(file,tag);
    }

    @Test
    public void testRelation () throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person1 = genealogy.findPerson("J");
        PersonIdentity person2 = genealogy.findPerson("E");
        genealogy.findRelation(person1,person2);
    }

    @Test
    public void testDescendants() throws Exception {
     BiologicalRelation relation = new BiologicalRelation();
     Genealogy genealogy = new Genealogy();
     PersonIdentity person = genealogy.findPerson("J");
     relation.descendents(person,10);
    }

    @Test
    public void testAncestors() throws Exception {
        BiologicalRelation relation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();
        PersonIdentity person = genealogy.findPerson("A");
        relation.ancestores(person,10);
    }

    @Test
    public void testFindPerson() throws Exception{
        Genealogy genealogy = new Genealogy();
        genealogy.findPerson("X");
    }

    @Test
    public void testFindName() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person = genealogy.findPerson("J");
        genealogy.findName(person);
    }

    @Test
    public void testFindTag() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByTag("keyur",null,null);
    }

    @Test
    public void testFindLocation() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        mediaArchive.findMediaByLocation("Toronto",null,null);
    }

    @Test
    public void testFindBiologicalFamilyMedia() throws Exception {
        MediaArchive mediaArchive = new MediaArchive();
        Genealogy genealogy  = new Genealogy();
        PersonIdentity person = genealogy.findPerson("D");
        mediaArchive.findBiologicalFamilyMedia(person);
    }
}