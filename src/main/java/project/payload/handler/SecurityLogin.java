package project.payload.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.payload.CommonResponse;
import project.payload.status.ErrorStatus;
import project.payload.status.SuccessStatus;

@Component
public class SecurityLogin {

    // 로그인 성공 시 JSON 응답 반환
    public static AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    CommonResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, null)
            ));
        };
    }

    //로그인 실패 시 JSON 응답 반환
    public static AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    CommonResponse.onError(ErrorStatus.UNAUTHORIZED)
            ));
        };
    }
}

