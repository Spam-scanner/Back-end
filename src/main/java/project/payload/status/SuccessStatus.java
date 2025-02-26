package project.payload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {
    //일반적 응답
    SUCCESS(HttpStatus.OK, "COMMON_200", "성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED,"COMMON_201", "성공적으로 생성하였습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT,"COMMON_204","성공적으로 삭제되었습니다."),

    LOGIN_SUCCESS(HttpStatus.OK, "2002", "로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "2003", "로그아웃 성공"),
    ANALYZE_SUCCESS(HttpStatus.OK,"2002", "분석 성공")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
