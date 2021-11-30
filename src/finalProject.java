import java.util.*;

public class finalProject {
    public static void main(String[] args) {
        FamilyTree familyTree = new FamilyTree();
        Member A = new Member("A",null);
        Member B = new Member("B",null);
        Member C = new Member("C",null);
        List<Member> DChilds = new ArrayList<>();
        DChilds.add(A);
        DChilds.add(B);
        DChilds.add(C);
        Member D = new Member("D",DChilds);
        Member E = new Member("E",null);
        List<Member> Gchilds = new ArrayList<>();
        Gchilds.add(E);
        Member G = new Member("G",Gchilds);
        Member F = new Member("F",null);
        List<Member> Hchilds = new ArrayList<>();
        Hchilds.add(F);
        Member H = new Member("H",Hchilds);
        List<Member> Ichilds = new ArrayList<>();
        Ichilds.add(G);
        Ichilds.add(H);
        Member I = new Member("I",Ichilds);
        List<Member> Jchilds = new ArrayList<>();
        Jchilds.add(D);
        Jchilds.add(I);
        familyTree.root.add(new Member("J",Jchilds));
        Member K = new Member("K",null);
        List<Member> Lchilds = new ArrayList<>();
        Lchilds.add(K);
        Member L = new Member("L",Lchilds);
        List<Member> Mchilds = new ArrayList<>();
        Mchilds.add(I);
        Mchilds.add(L);
        familyTree.root.add(new Member("M",Mchilds));
        familyTree.findPerson(E);
    }
}
