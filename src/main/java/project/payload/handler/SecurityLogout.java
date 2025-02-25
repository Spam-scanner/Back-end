package project.payload.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import project.payload.CommonResponse;
import project.payload.status.SuccessStatus;

@Component
public class SecurityLogout {
    private final ObjectMapper objectMapper = new ObjectMapper();

    //로그아웃 성공 시 JSON 응답 반환
    public static LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    CommonResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS, null)
            ));
        };
    }
}
