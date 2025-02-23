package project.repository;

import project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByuserID(String userID);
    boolean existsByUserID(String userID);
}
