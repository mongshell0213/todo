package hello.todo.domain.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.todo.domain.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil{
    public static Connection getConnection() throws SQLException{
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("connection = {}", connection);
            return connection;
        }catch(SQLException e){
            log.error("error = {}",e);
            throw new SQLException(e);
        }
    }
}
