package com.example.onlinefigurineshopping5.service;

import com.example.onlinefigurineshopping5.entity.User;
import java.util.List;

public interface UserService {
    User register(User user);
    User login(String username, String password);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    User updateProfile(User user);
    boolean changePassword(Long id, String oldPassword, String newPassword);
}