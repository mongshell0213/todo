package hello.todo.domain.member;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberRepository {
    private static final Map<String, Member> store = new HashMap<>();

    public Member save(Member member){
        store.put(member.getId(),member);
        return member;
    }

    public Optional<Member> findById(String id){
        return findAll().stream()
                .filter(m->m.getId().equals(id))
                .findFirst();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void updateMember(String id, Member updated){
        Member member = findById(id).get();
        member.setId(updated.getId());
        member.setPassWord(updated.getPassWord());
    }

    public void deleteMember(String id){
        store.remove(id);
    }
}
