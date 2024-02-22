package hello.todo.web.member;

import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String memberJoinForm(Model model){
        model.addAttribute("member",new Member());
        return "members/joinForm";
    }

    @PostMapping("/join")
    public String memberJoin(@Validated @ModelAttribute Member member, BindingResult result){
        if(memberRepository.findById(member.getId()).orElse(null)!=null){
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
}
