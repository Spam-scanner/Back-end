package project.payload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.payload.status.BaseStatus;

@Getter
@Builder
public class CommonResponse<T> {
    private final int status; //HTTP 상태코드
    private final String code; // 상세 상태
    private final String message;
    private final T data;

    public static <T> CommonResponse<T> onSuccess(BaseStatus status, T data) {
        return CommonResponse.<T>builder()
                .status(status.getHttpStatus().value())
                .code(status.getCode())
                .message(status.getMessage())
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> onError(BaseStatus status) {
        return CommonResponse.<T>builder()
                .status(status.getHttpStatus().value())
                .code(status.getCode())
                .message(status.getMessage())
                .data(null)
                .build();
    }
}

