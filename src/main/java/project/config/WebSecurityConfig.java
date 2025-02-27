package project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import project.payload.CommonResponse;
import project.payload.handler.SecurityLogin;
import project.payload.handler.SecurityLogout;
import project.payload.status.ErrorStatus;
import project.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.payload.status.*;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();  //JSON 변환

    //Spring Security자제에서 자동으로 로그인과 로그아웃을 처리한다고 합니당...

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");  //정적 파일 요청 무시
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().ignoringRequestMatchers("/api/**")  //API 요청 CSRF 비활성화
                .and()
                .authorizeRequests()
                .requestMatchers("/api/users", "/api/users/login", "/api/analyze").permitAll()
                .anyRequest().authenticated()
                .and()
                //로그인 구현
                .formLogin()
                    .loginProcessingUrl("/api/users/login")  //로그인 엔드포인트
                    .usernameParameter("userID")
                    .successHandler(SecurityLogin.authenticationSuccessHandler())  //로그인 성공 시 JSON 응답
                    .failureHandler(SecurityLogin.authenticationFailureHandler())  //로그인 실패 시 JSON 응답
                .and()
                //로그아웃 구현
                .logout()
                    .logoutUrl("/api/users/logout")
                    .logoutSuccessHandler(SecurityLogout.logoutSuccessHandler())  //로그아웃 성공 시 JSON 응답
                    .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {  //인증 실패 시 JSON 응답
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(
                            CommonResponse.onError(ErrorStatus.UNAUTHORIZED)
                    ));
                })
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
