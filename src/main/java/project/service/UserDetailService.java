package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.domain.User;
import project.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String userID){
        System.out.println(userID);
        return userRepository.findByuserID(userID)
                .orElseThrow(()->new IllegalArgumentException(userID));
    }
}

