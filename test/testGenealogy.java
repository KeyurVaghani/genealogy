import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import persistant.Genealogy;
import persistant.PersonIdentity;

import java.util.HashMap;

public class testGenealogy {

    @Test
    public void testAddPersonWithNullPerson() {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Genealogy genealogy = new Genealogy();
                PersonIdentity A = genealogy.addPerson(null);
            }
        });

    }

    @Test
    public void testAddPersonWithValidPersonName() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity A = genealogy.addPerson("testPerson1");
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
    public void testFindPersonWhenPersonNotExist() {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Genealogy genealogy = new Genealogy();
                genealogy.findPerson("X");
            }
        });
    }

    @Test
    public void testFindPersonWhenPersonExist() throws Exception {
        Genealogy genealogy = new Genealogy();
        genealogy.findPerson("M");
    }

    @Test
    public void testFindName() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person = genealogy.findPerson("J");
        genealogy.findName(person);
    }

    @Test
    public void testRelation () throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person1 = genealogy.findPerson("J");
        PersonIdentity person2 = genealogy.findPerson("E");
        genealogy.findRelation(person1,person2);
    }

    @Test
    public void testNoteAndReferences() throws Exception {
        Genealogy genealogy = new Genealogy();
        PersonIdentity person = genealogy.findPerson("A");
        genealogy.notesAndReferences(person);
    }
}
