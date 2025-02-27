package project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.User;
import project.dto.AddUserRequest;
import project.dto.AddUserResponse;
import project.dto.AnalyzeResultResponse;
import project.payload.CommonResponse;
import project.service.AnalysisService;
import project.service.UserService;
import project.payload.status.*;

@RequiredArgsConstructor
//@Controller //HTML을 반환
@RestController //Http 응답 데이터를 JSON 형식으로 반환
@RequestMapping("/api")
public class UserApiController {
    private final UserService userService;
    private final AnalysisService analysisService;

    //회원가입
    @PostMapping("/users")
    public CommonResponse<AddUserResponse> signup(@RequestBody @Valid AddUserRequest request) {
        // 아이디 중복 체크
        if (userService.isIdExists(request.getUserID())) {
            return CommonResponse.onError(ErrorStatus.ID_ALREADY_EXIST);
        }

        // 회원가입 성공
        User user = userService.save(request);
        AddUserResponse responseDto = new AddUserResponse(user);
        return CommonResponse.onSuccess(SuccessStatus.CREATED, responseDto);
    }


    @PostMapping("/analyze")
    public CommonResponse<AnalyzeResultResponse.ResultMessage> analyzeText(
            @RequestParam("inputText") String inputText) {
        //null이거나 빈문자열이 온다면
        if(analysisService.isInputExists(inputText)) {
            return CommonResponse.onError(ErrorStatus.INVALID_INPUT);
        }
        AnalyzeResultResponse.ResultMessage result = analysisService.analyzeText(inputText);
        return CommonResponse.onSuccess(SuccessStatus.ANALYZE_SUCCESS, result);
    }

}
