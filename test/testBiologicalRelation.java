import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import persistant.BiologicalRelation;
import persistant.Genealogy;
import persistant.PersonIdentity;

public class testBiologicalRelation {
    @Test
    public void testRecordChild() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();

        PersonIdentity parent = genealogy.findPerson("A");
        PersonIdentity child = genealogy.findPerson("B");

        biologicalRelation.recordChild(parent, child);
    }

    @Test
    public void testRecordChildWhenParentAndChildAreSame() throws Exception {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                BiologicalRelation biologicalRelation = new BiologicalRelation();
                Genealogy genealogy = new Genealogy();

                PersonIdentity parent = genealogy.findPerson("A");
                PersonIdentity child = genealogy.findPerson("A");

                biologicalRelation.recordChild(parent, child);
            }
        });
    }

    @Test
    public void testRecordPartnering() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();

        PersonIdentity partner1 = genealogy.findPerson("C");
        PersonIdentity partner2 = genealogy.findPerson("D");

        biologicalRelation.recordPartnering(partner1, partner2);
    }

    @Test
    public void testRecordDissolute() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();

        PersonIdentity partner1 = genealogy.findPerson("C");
        PersonIdentity partner2 = genealogy.findPerson("D");

        biologicalRelation.recordDissolute(partner1, partner2);
    }

    @Test
    public void testDescendents() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();

        PersonIdentity person = genealogy.findPerson("A");

        biologicalRelation.descendents(person, 2);
    }

    @Test
    public void testAncestores() throws Exception {
        BiologicalRelation biologicalRelation = new BiologicalRelation();
        Genealogy genealogy = new Genealogy();

        PersonIdentity person = genealogy.findPerson("C");

        biologicalRelation.ancestores(person, 2);
    }
}
