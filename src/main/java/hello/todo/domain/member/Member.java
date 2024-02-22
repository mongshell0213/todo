package hello.todo.domain.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 서비스에 접근할 수 있는 멤버
 * 아이디
 * 비밀번호
 */

@Setter @Getter
public class Member {

    @NotBlank(message="아이디는 공백일 수 없습니다.")
    private String id;

    @NotBlank(message="비밀번호는 공백일 수 없습니다.")
    @Length(min=8,message="비밀번호는 최소 8자리 이상이어야 합니다.")
    private String passWord;

    public Member(String id, String passWord){
        this.id = id;
        this.passWord = passWord;
    }
    public Member(){

    }
}
