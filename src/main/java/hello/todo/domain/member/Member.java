package hello.todo.domain.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 서비스에 접근할 수 있는 멤버
 * 아이디
 * 비밀번호
 */

@Setter @Getter
public class Member {
    private String id;
    private String passWord;

    public Member(String id, String passWord){
        this.id = id;
        this.passWord = passWord;
    }
    public Member(){

    }
}
