import java.util.*;

public class Member {
     List<Member> parents = new ArrayList<>();
     List<Member> childs = new ArrayList<>();
     String name;

    Member(String name,List<Member> childs){
        this.name = name;
        this.childs = childs;
    }
}       