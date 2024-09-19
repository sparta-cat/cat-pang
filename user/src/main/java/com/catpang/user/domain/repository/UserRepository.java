package com.catpang.user.domain.repository;

import com.catpang.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String userId);

    Optional<User> findBySlackId(String slackId);
}
