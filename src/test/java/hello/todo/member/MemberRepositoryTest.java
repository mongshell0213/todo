package hello.todo.member;

import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberRepositoryTest {
    private static final MemberRepository memberRepository = new MemberRepository();

    @Test
    public void saveFindTest(){
        Member member = new Member("spring","hello");
        memberRepository.save(member);
        assertThat(memberRepository.findById("spring")).isEqualTo(member);
    }

    @Test
    public void findAllTest(){
        Member member1 = new Member("spring","hello");
        Member member2 = new Member("boot","hello");
        memberRepository.save(member1);
        memberRepository.save(member2);
        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void updateTest(){
        Member member = new Member("spring","hello");
        memberRepository.save(member);

        Member updatedMember = new Member("boot","helloBoot");
        memberRepository.updateMember(member.getId(), updatedMember);
        assertThat(member.getId()).isEqualTo("boot");
        assertThat(member.getPassWord()).isEqualTo("helloBoot");
    }

    @Test
    public void deleteTest(){
        Member member = new Member("spring","hello");
        memberRepository.save(member);
        memberRepository.deleteMember(member.getId());

        assertThat(memberRepository.findById(member.getId())).isNull();
    }
}
