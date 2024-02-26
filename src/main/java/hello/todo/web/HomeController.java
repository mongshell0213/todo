package hello.todo.web;

import hello.todo.domain.member.Member;
import hello.todo.web.argumentResolver.Login;
import hello.todo.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    //@GetMapping("/")
    public String home(){
        return "home";
    }

    //@GetMapping("/")
    public String homeV2(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session==null)
            return "home";
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(member==null)
            return "home";

        model.addAttribute("member",member);
        return "loginHome";
    }
    @GetMapping("/")
    public String homeV3(@Login Member member, Model model){
        log.info("member={}",member);
        if(member==null)
            return "home";
        else{
            model.addAttribute("member",member);
            return "loginHome";
        }
    }
}
