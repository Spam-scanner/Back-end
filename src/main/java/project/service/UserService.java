package project.service;

import lombok.RequiredArgsConstructor;
import project.domain.User;
import project.dto.AddUserRequest;
import project.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .userID(dto.getUserID())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .gender(dto.isGender())
                .build()).getId();
    }
    public boolean isIdExists(String userID){
        return userRepository.existsByUserID(userID);
    }
    public boolean isEmailExists(String email){
        return userRepository.existsByEmail(email);
    }
}
