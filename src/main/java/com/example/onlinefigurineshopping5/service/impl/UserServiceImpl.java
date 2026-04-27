package com.example.onlinefigurineshopping5.service.impl;

import com.example.onlinefigurineshopping5.entity.User;
import com.example.onlinefigurineshopping5.mapper.UserMapper;
import com.example.onlinefigurineshopping5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // MD5加密密码
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        // 密码加密
        user.setPassword(encryptPassword(user.getPassword()));
        user.setRole("user");
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(encryptPassword(password))) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User updateProfile(User user) {
        userMapper.update(user);
        return userMapper.findById(user.getId());
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.findById(id);
        if (!user.getPassword().equals(encryptPassword(oldPassword))) {
            return false;
        }
        userMapper.updatePassword(id, encryptPassword(newPassword));
        return true;
    }
}