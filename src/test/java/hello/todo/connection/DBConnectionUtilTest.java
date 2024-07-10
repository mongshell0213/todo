package hello.todo.connection;

import hello.todo.domain.connection.DBConnectionUtil;
import hello.todo.domain.member.MemberDBRepositoryV0;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class DBConnectionUtilTest {
    private DBConnectionUtil util;
    @Test
    public void connectTest(){
        util = new DBConnectionUtil();
        try {
            Connection connection = util.getConnection();
            assertThat(connection).isNotNull();
            log.info("connection = {}",connection);
        }catch(SQLException e){
            log.error("SQLException error");
        }
    }
}
