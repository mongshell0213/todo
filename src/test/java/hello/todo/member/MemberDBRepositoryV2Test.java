package hello.todo.member;

import com.zaxxer.hikari.HikariDataSource;
import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberDBRepositoryV1;
import hello.todo.domain.member.MemberDBRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static hello.todo.domain.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@Slf4j
public class MemberDBRepositoryV2Test {
     MemberDBRepositoryV2 repository;
     HikariDataSource dataSource;

     @BeforeEach
     void setting(){
         dataSource = new HikariDataSource();
         dataSource.setJdbcUrl(URL);
         dataSource.setUsername(USERNAME);
         dataSource.setPassword(PASSWORD);

         repository = new MemberDBRepositoryV2(dataSource);
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
    void updateTest() throws SQLException {
         Member member1 = new Member("spring","12345678");
         Member updateMember = new Member("summer","87654321");

         repository.save(member1);
         repository.updateMember(member1.getId(), updateMember);
         Member testMember = repository.findById(updateMember.getId()).get();
         assertThat(testMember.getId()).isEqualTo("summer");
         assertThat(testMember.getPassWord()).isEqualTo("87654321");
    }

    @Test
    void updateTransactionTest() throws SQLException {
         Member member = new Member("spring","12345678");
         Member updateMember = new Member("summer","87654321");

         repository.save(member);
         Connection con = dataSource.getConnection();
         try {
             con.setAutoCommit(false);
             updateLogic(con, member, updateMember);
             con.commit();
         }catch(Exception e){
             con.rollback();
         }finally{
             con.setAutoCommit(false);
             con.close();
         }
         //멤버의 아이디가 ex인 경우 롤백이 일어난다.
    }
    void updateLogic(Connection con, Member member, Member updateMember) throws Exception {
         repository.updateMemberId(con, member.getId(), updateMember.getId());
         if(member.getId() == "ex")
             throw new Exception();
         repository.updateMemberPassword(con, updateMember.getId(), updateMember.getPassWord());
    }
}
