package com.example.e_commerce.service;
import com.example.e_commerce.entity.User;

import java.util.Optional;

public interface UserService {
    User signUp(User user);
    Optional<User> signIn(String email, String password);
}