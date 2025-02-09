package project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String userID;
    private String email;
    private String password;
    private boolean gender;
}
