package hello.todo.web.login;

import hello.todo.domain.login.LoginForm;
import hello.todo.domain.member.Member;
import hello.todo.domain.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm loginForm){
        return "login/login-form";
    }
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult result
            , HttpServletRequest request, @RequestParam(value = "redirectURL",defaultValue = "/basic/items")String redirectURL){
        log.info("loginForm={}",loginForm);
        Member loginMember = loginService.login(loginForm.getId(),loginForm.getPassWord());
        if(loginMember==null){
            result.reject("loginFail","아이디나 비밀번호가 맞지 않습니다.");
        }
        if(result.hasErrors()){
            return "login/login-form";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        return "redirect:"+redirectURL;
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
            log.info("logout");
        }
        return "redirect:/";
    }
}
