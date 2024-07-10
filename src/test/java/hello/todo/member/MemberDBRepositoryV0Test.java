package hello.todo.member;

import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberDBRepositoryV0;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
public class MemberDBRepositoryV0Test {
     MemberDBRepositoryV0 repository = new MemberDBRepositoryV0();
     @BeforeEach
     void setting(){
         repository.deleteMember("spring");
         repository.deleteMember("summer");
     }

    @Test
    void saveFindTest(){
        Member member = new Member("spring","12345678");
        log.info("member = {}",member.getId());
        repository.save(member);
        Member findMember = repository.findById(member.getId()).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void findAllTest(){
         Member member1 = new Member("spring","12345678");
         Member member2 = new Member("summer","12345678");
         repository.save(member1);
         repository.save(member2);

        List<Member> finds = repository.findAll();
        assertThat(finds.get(0).getId()).isEqualTo(member1.getId());
        assertThat(finds.get(1).getId()).isEqualTo(member2.getId());
    }

    @Test
    void updateTest(){
         Member member1 = new Member("spring","12345678");
         Member updateMember = new Member("summer","87654321");

         repository.save(member1);
         repository.updateMember(member1.getId(), updateMember);
         Member testMember = repository.findById(updateMember.getId()).get();
         assertThat(testMember.getId()).isEqualTo("summer");
        assertThat(testMember.getPassWord()).isEqualTo("87654321");
    }
}
