package com.example.onlinefigurineshopping5.mapper;

import com.example.onlinefigurineshopping5.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Insert("INSERT INTO order_item(order_id, figure_id, figure_name, price, quantity, total_price) " +
            "VALUES(#{orderId}, #{figureId}, #{figureName}, #{price}, #{quantity}, #{totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem orderItem);

    @Insert({
            "<script>",
            "INSERT INTO order_item(order_id, figure_id, figure_name, price, quantity, total_price) VALUES ",
            "<foreach collection='items' item='item' separator=','>",
            "(#{item.orderId}, #{item.figureId}, #{item.figureName}, #{item.price}, #{item.quantity}, #{item.totalPrice})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(@Param("items") List<OrderItem> items);

    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(Long orderId);

    @Select("SELECT * FROM order_item WHERE id = #{id}")
    OrderItem findById(Long id);
}