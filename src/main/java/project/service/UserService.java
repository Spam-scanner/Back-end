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

    public User save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .userID(dto.getUserID())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .age(dto.getAge())
                .build());
    }
    public boolean isIdExists(String userID){
        return userRepository.existsByUserID(userID);
    }
}
