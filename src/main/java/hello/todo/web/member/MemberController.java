package hello.todo.web.member;

import hello.todo.domain.member.*;
import hello.todo.web.login.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    private final MemberDBRepositoryV2 memberRepository;

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
    @PostMapping("/update")
    public String memberUpdate(@ModelAttribute("updateMember") Member updateMember,
                               @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false) Member loginMember
                                ,Model model) throws SQLException {
        memberRepository.updateMember(loginMember.getId(), updateMember);
        return "redirect:/";
    }
}
