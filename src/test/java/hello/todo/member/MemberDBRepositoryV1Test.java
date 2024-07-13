package hello.todo.member;

import com.zaxxer.hikari.HikariDataSource;
import hello.todo.domain.connection.ConnectionConst;
import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberDBRepositoryV0;
import hello.todo.domain.member.MemberDBRepositoryV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static hello.todo.domain.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@Slf4j
public class MemberDBRepositoryV1Test {
     MemberDBRepositoryV1 repository;
     @BeforeEach
     void setting(){
         HikariDataSource dataSource = new HikariDataSource();
         dataSource.setJdbcUrl(URL);
         dataSource.setUsername(USERNAME);
         dataSource.setPassword(PASSWORD);

         repository = new MemberDBRepositoryV1(dataSource);
         //repository.deleteMember("spring");
         //repository.deleteMember("summer");
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
