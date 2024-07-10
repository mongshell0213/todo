package hello.todo.domain.member;

import hello.todo.domain.connection.DBConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MemberDBRepositoryV0 {

    public Member save(Member member){
        String sql = "insert into joinMember(member_id,password) values (?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
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
            con = getConnection();
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
            con = getConnection();
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
    public void updateMember(String id, Member updated){
        updateMemberId(id,updated.getId());
        updateMemberPassword(updated.getId(),updated.getPassWord());
    }
    public void updateMemberId(String id, String changeId){
        String sql = "update joinMember set member_id = ? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,changeId);
            pstmt.setString(2,id);
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("update error",e);
        }finally {
            close(con, pstmt, null);
        }
    }
    public void updateMemberPassword(String id, String changePasswd){
        String sql = "update joinMember set password = ? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt1 = null;
        try{
            con = getConnection();
            pstmt1 = con.prepareStatement(sql);
            pstmt1.setString(1,changePasswd);
            pstmt1.setString(2,id);
            pstmt1.executeUpdate();
        }catch(SQLException e){
            log.error("update error",e);
        } finally{
            close(con, pstmt1, null);
        }
    }
    public void deleteMember(String id){
        String sql = "delete from joinMember where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("delete error",e);
        }finally {
            close(con, pstmt, null);
        }
    }

    private Connection getConnection(){
        try {
            Connection con = DBConnectionUtil.getConnection();
            return con;
        }catch(SQLException e){
            throw new IllegalArgumentException(e);
        }
    }
    private void close(Connection con, PreparedStatement stmt, ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            }catch(SQLException e){
                log.error("rs close error",e);
            }
        }
        if(stmt != null){
            try{
                stmt.close();
            }catch(SQLException e){
                log.error("stmt close error",e);
            }
        }
        if(con != null){
            try{
                con.close();
            }catch(SQLException e){
                log.error("connection close error",e);
            }
        }
    }
}
