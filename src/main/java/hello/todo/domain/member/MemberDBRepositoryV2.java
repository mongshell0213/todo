package hello.todo.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 멤버 업데이트는 아이디와 비밀번호를 업데이트 한다.
 * 에러 발생 시 아이디만 변경될 가능성이 존재한다.
 * 따라서 트랜잭션을 사용하여 커밋과 롤백으로 제어한다.
 */
@Repository
@Slf4j
public class MemberDBRepositoryV2 {
    private final DataSource dataSource;
    public MemberDBRepositoryV2(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public Member save(Member member){
        String sql = "insert into joinMember(member_id,password) values (?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,member.getId());
            pstmt.setString(2,member.getPassWord());
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("error",e);
        }finally{
            close(con,pstmt,null);
        }
        return member;
    }
    public Optional<Member> findById(String id){
        String sql = "select * from joinMember where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Member member = null;

        try{
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                member = new Member();
                member.setId(rs.getString("member_id"));
                member.setPassWord(rs.getString("password"));

            }
        }catch(SQLException e) {
            log.error("error", e);
        }finally{
            close(con,pstmt,rs);
        }
        return Optional.ofNullable(member);
    }
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String sql = "select * from joinMember";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                Member member = new Member();
                member.setId(rs.getString("member_id"));
                member.setPassWord(rs.getString("password"));
                members.add(member);
            }
        }catch(SQLException e){
            log.error("error",e);
        }finally{
            close(con,pstmt,rs);
        }
        return members;
    }
    public void updateMember(String id, Member updated) throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            updateMemberId(con,id,updated.getId());
            updateMemberPassword(con,updated.getId(),updated.getPassWord());
            con.commit();
        }catch(SQLException e){
            con.rollback();
            log.error("error",e);
        }finally{
            con.setAutoCommit(true);
            con.close();
        }
    }
    public void updateMemberId(Connection con, String id, String changeId){
        String sql = "update joinMember set member_id = ? where member_id = ?";
        PreparedStatement pstmt = null;
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,changeId);
            pstmt.setString(2,id);
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("update error",e);
        }finally {
            JdbcUtils.closeStatement(pstmt);
            //close(con, pstmt, null);
        }
    }
    public void updateMemberPassword(Connection con, String id, String changePasswd){
        String sql = "update joinMember set password = ? where member_id = ?";
        PreparedStatement pstmt = null;
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,changePasswd);
            pstmt.setString(2,id);
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("update error",e);
        } finally{
            //close(con, pstmt1, null);
            JdbcUtils.closeStatement(pstmt);
        }
    }
    public void deleteMember(String id){
        String sql = "delete from joinMember where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("delete error",e);
        }finally {
            close(con, pstmt, null);
        }
    }

    /*
    private Connection getConnection(){
        try {
            Connection con = DBConnectionUtil.getConnection();
            return con;
        }catch(SQLException e){
            throw new IllegalArgumentException(e);
        }
    }
     */

    private void close(Connection con, PreparedStatement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
}
