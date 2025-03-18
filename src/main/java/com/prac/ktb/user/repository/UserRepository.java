package com.prac.ktb.user.repository;

import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(UserResponseDto userResDto);
    // List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
