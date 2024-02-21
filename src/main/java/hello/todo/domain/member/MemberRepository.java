package hello.todo.domain.member;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {
    private static final Map<String, Member> store = new HashMap<>();

    public Member save(Member member){
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(String id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void updateMember(String id, Member updated){
        Member member = findById(id);
        member.setId(updated.getId());
        member.setPassWord(updated.getPassWord());
    }

    public void deleteMember(String id){
        store.remove(id);
    }
}
