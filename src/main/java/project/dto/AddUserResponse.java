package project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.domain.User;

@Getter
@AllArgsConstructor
public class AddUserResponse{
    private String userID;
    private int age;

    public AddUserResponse(User user) {
        this.userID = user.getUserID();
        this.age = user.getAge();
    }
}
