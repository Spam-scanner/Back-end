package project.payload;

import project.payload.status.BaseStatus;
import project.payload.status.ErrorStatus;
import project.payload.status.SuccessStatus;

import java.util.Arrays;

public class ResponseStatusConverter {
    public static BaseStatus fromCode(String code) {
        return Arrays.stream(SuccessStatus.values())
                .filter(status -> status.getCode().equals(code))
                .map(status -> (BaseStatus) status)
                //SuccessStatus에서 BaseStatus로 변환
                .findFirst() // stream순서에 따라 필터에 일치하는 요소 1개 리턴
                .orElseGet(() -> Arrays.stream(ErrorStatus.values())
                        .filter(status -> status.getCode().equals(code))
                        .map(status -> (BaseStatus) status)
                        //ErrorStatus에서 BaseStatus로 변환
                        .findFirst()
                        .orElse(null));
    }
}
