package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.domain.User;
import project.dto.AddUserRequest;
import project.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .userID(dto.getUserID())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .age(dto.getAge())
                .build()).getId();
    }
    public boolean isIdExists(String userID){
        return userRepository.existsByUserID(userID);
    }
}
