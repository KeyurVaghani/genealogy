import org.junit.jupiter.api.*;
import persistant.Genealogy;
import persistant.PersonIdentity;
import persistant.Relation;

import java.sql.SQLException;

public class testFiles {
    @Test
    public void testAddPerson() throws SQLException, ClassNotFoundException {
        PersonIdentity A = new PersonIdentity("A");
        PersonIdentity B = new PersonIdentity("B");
        PersonIdentity C = new PersonIdentity("C");
        PersonIdentity D = new PersonIdentity("D");
        PersonIdentity J = new PersonIdentity("J");
        PersonIdentity I = new PersonIdentity("I");
        PersonIdentity G = new PersonIdentity("G");
        PersonIdentity H = new PersonIdentity("H");
        PersonIdentity E = new PersonIdentity("E");
        PersonIdentity F = new PersonIdentity("F");
        PersonIdentity M = new PersonIdentity("M");
        PersonIdentity L = new PersonIdentity("L");
        PersonIdentity K = new PersonIdentity("K");

        Relation relation = new Relation();
        Genealogy genealogy = new Genealogy();
        A.addPerson("A");
        A.addPerson("B");
        A.addPerson("C");
        A.addPerson("D");
        A.addPerson("J");
        A.addPerson("I");
        A.addPerson("G");
        A.addPerson("H");
        A.addPerson("E");
        A.addPerson("F");
        A.addPerson("M");
        A.addPerson("L");
        A.addPerson("K");

        relation.recordChild(D,A);
        relation.recordChild(D,B);
        relation.recordChild(D,C);
        relation.recordChild(J,D);
        relation.recordChild(G,E);
        relation.recordChild(H,F);
        relation.recordChild(I,G);
        relation.recordChild(I,H);
        relation.recordChild(J,I);
        relation.recordChild(M,I);
        relation.recordChild(M,L);
        relation.recordChild(L,K);

    }
}
