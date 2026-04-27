package com.example.onlinefigurineshopping5.mapper;

import com.example.onlinefigurineshopping5.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(username, password, email, phone, address, role) " +
            "VALUES(#{username}, #{password}, #{email}, #{phone}, #{address}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Update("UPDATE user SET phone = #{phone}, address = #{address} WHERE id = #{id}")
    int update(User user);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);
}