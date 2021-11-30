import java.util.*;

public class FamilyTree {
    public List<Member> root = new ArrayList();

    public void addPerson(String name){
            Member member = new Member(name,null);
            this.root.add(member);
    }

    Boolean recordChild(Member child,Member parent){
        child.parents.add(parent);
        Member X = findPerson(child);
        X.parents.add(child);
        return false;
    }

    public Member findPerson(Member child){
        Member person = null;
        for (Member member : root) {
            person = searchPerson(member, child);
            if(person != null){
            return person;
            }
        }
        return person;
    }

    public Member searchPerson(Member member,Member child){
        if(member.name == child.name){
            return member;
        }
        if(member == null || member.childs == null){
            return null;
        }
        for (int i = 0;i < member.childs.size() ;i++){
            Member person = searchPerson(member.childs.get(i),child);
            if(person != null){
                return person;
            }
        }
        return null;
    }
}
