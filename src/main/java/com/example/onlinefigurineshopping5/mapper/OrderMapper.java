package com.example.onlinefigurineshopping5.mapper;

import com.example.onlinefigurineshopping5.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO `order`(order_no, user_id, total_amount, status, " +
            "receiver_name, receiver_phone, receiver_address) " +
            "VALUES(#{orderNo}, #{userId}, #{totalAmount}, #{status}, " +
            "#{receiverName}, #{receiverPhone}, #{receiverAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM `order` WHERE id = #{id}")
    Order findById(Long id);

    @Select("SELECT * FROM `order` WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    @Select("SELECT * FROM `order` WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> findByUserId(Long userId);

    @Select("SELECT * FROM `order` ORDER BY create_time DESC")
    List<Order> findAll();

    @Update("UPDATE `order` SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE `order` SET status = #{status}, pay_time = NOW() WHERE id = #{id}")
    int pay(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT * FROM `order` WHERE user_id = #{userId} AND status = #{status}")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}