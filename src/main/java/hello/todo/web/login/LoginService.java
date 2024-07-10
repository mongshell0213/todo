package hello.todo.web.login;

import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberDBRepositoryV0;
import hello.todo.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    //private final MemberRepository memberRepository;
    private final MemberDBRepositoryV0 memberRepository;

    public Member login(String id,String passWord){
        return memberRepository.findById(id)
                .filter(m->m.getPassWord().equals(passWord)).orElse(null);
    }
}
