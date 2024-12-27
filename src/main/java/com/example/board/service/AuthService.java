package com.example.board.service;

import com.example.board.model.User;
import com.example.board.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'";
        List<User> users = entityManager.createNativeQuery(sql, User.class).getResultList();
        return users.stream().findFirst();
    }
    
    public Optional<String> findPassword(String username, String department, String role) {
        // SQL 쿼리를 문자열 결합 방식으로 생성
        String sql = "SELECT password FROM user WHERE username = '" + username + 
                     "' AND department = '" + department + 
                     "' AND role = '" + role + "'";

        // Native Query 실행
        List<String> passwords = entityManager.createNativeQuery(sql).getResultList();
        return passwords.stream().findFirst();
    }

}
