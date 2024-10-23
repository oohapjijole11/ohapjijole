package com.sparta.final_project.domain.user.repository;

import com.sparta.final_project.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(@NotBlank @Email String email);

    Optional<User> findByEmail(@NotBlank @Email String email);

    Optional<User> findByName(String name);

    Optional<User> findUserByEmailAndIsdeleted(String email, Boolean isDeleted);
}

