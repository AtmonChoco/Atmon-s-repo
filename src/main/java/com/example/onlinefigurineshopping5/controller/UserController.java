package com.example.onlinefigurineshopping5.controller;

import com.example.onlinefigurineshopping5.common.Result;
import com.example.onlinefigurineshopping5.entity.User;
import com.example.onlinefigurineshopping5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            Map<String, Object> data = new HashMap<>();
            data.put("id", newUser.getId());
            data.put("username", newUser.getUsername());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 用户登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String password = params.get("password");
            User user = userService.login(username, password);
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 获取用户信息
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    // 获取所有用户
    @GetMapping("/list")
    public Result<List<User>> listAll() {
        List<User> users = userService.findAll();
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }

    // 更新个人信息
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user) {
        try {
            User updated = userService.updateProfile(user);
            updated.setPassword(null);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 修改密码
    @PutMapping("/password")
    public Result<String> changePassword(@RequestBody Map<String, String> params) {
        try {
            Long id = Long.valueOf(params.get("id"));
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("原密码错误");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}