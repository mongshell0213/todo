package hello.todo.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("LoginInterceptor");
        HttpSession session = request.getSession(false);

        if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        return true;
    }
}
