package project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.domain.User;
import project.dto.AddUserRequest;
import project.dto.AddUserResponse;
import project.payload.CommonResponse;
import project.service.UserService;
import project.payload.status.*;

@RequiredArgsConstructor
//@Controller //HTML을 반환
@RestController //Http 응답 데이터를 JSON 형식으로 반환
@RequestMapping("/api")
public class UserApiController {
    private final UserService userService;

    //회원가입
    @PostMapping("/users")
    public ResponseEntity<CommonResponse<AddUserResponse>> signup(@RequestBody @Valid AddUserRequest request) {
        // 아이디 중복 체크
        if (userService.isIdExists(request.getUserID())) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.onError(ErrorStatus.ID_ALREADY_EXIST));
        }

        // 회원가입 성공
        User user = userService.save(request);
        AddUserResponse responseDto = new AddUserResponse(user);
        return ResponseEntity.ok(CommonResponse.onSuccess(SuccessStatus.CREATED, responseDto));
    }

    //이 밑은 이전 코드인데 혹시나 나중에 프론트랑 연결할때 필요할까봐 주석처리로 남겨두었습니다.
//    public String signup(AddUserRequest request, @ModelAttribute User user,Model model){
//        //아이디 중복S
//       if (userService.isIdExists(user.getUserID())) {
//            model.addAttribute("iderror", true);
//            return "signup";
//        }
//       //이메일 중복
//        if (userService.isEmailExists(user.getEmail())) {
//            model.addAttribute("emailerror",true);
//            return "signup";
//        }
//        //회원가입 성공
//        userService.save(request);
//        return "redirect:/login";
//    }
}
