package hello.todo.web.member;

import hello.todo.domain.member.*;
import hello.todo.web.login.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    //private final MemberRepository memberRepository;
    //private final MemberDBRepositoryV0 memberRepository;
    //private final MemberDBRepositoryV1 memberRepository;
    //private final MemberDBRepositoryV2 memberRepository;
    private final MemberDBRepositoryV3 memberRepository;
    //private final PlatformTransactionManager transactionManager;

    @GetMapping("/join")
    public String memberJoinForm(Model model){
        model.addAttribute("member",new Member());
        return "members/joinForm";
    }

    @PostMapping("/join")
    public String memberJoin(@Validated @ModelAttribute Member member, BindingResult result){
        if(memberRepository.findById(member.getId()).orElse(null)!=null){
        //if(memberRepository.findById(member.getId()).get() != null){
            result.rejectValue("id","overlap","아이디가 중복됩니다.");
        }
        if(result.hasErrors()){
            log.info("errors={}",result);
            return"members/joinForm";
        }

        memberRepository.save(member);
        //log.info("SaveMember={}",memberRepository.findById(member.getId()).getId());
        return "redirect:/";
    }
    @GetMapping("/update")
    public String memberUpdateForm(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        model.addAttribute("loginMember",loginMember);
        model.addAttribute("updateMember",new Member());
        return "members/updateForm";
    }

    /**
     * @Transactional로 스프링 AOP를 활용하거나, 트랜잭션 매니저를 활용 할 수 있다.
     */
    @PostMapping("/update")
    @Transactional // 스프링 AOP 활용
    public String memberUpdate(@ModelAttribute("updateMember") Member updateMember,
                               @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws SQLException {
        // 트랜잭션 매니저 사용
        /*
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            memberRepository.updateMemberId(loginMember.getId(), updateMember.getId());
            memberRepository.updateMemberPassword(updateMember.getId(),updateMember.getPassWord());
            transactionManager.commit(status);
        }catch(Exception e){
            log.error("update error",e);
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
         */
        memberRepository.updateMemberId(loginMember.getId(), updateMember.getId());
        memberRepository.updateMemberPassword(updateMember.getId(),updateMember.getPassWord());
        return "redirect:/";
    }
}
