package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickName(String nickName);

    Optional<User> findByUsername(String userName);
}
