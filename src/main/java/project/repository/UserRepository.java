package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByuserID(String userID);
    boolean existsByUserID(String userID);
}
