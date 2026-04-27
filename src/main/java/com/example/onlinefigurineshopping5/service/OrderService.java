package com.example.onlinefigurineshopping5.service;

import com.example.onlinefigurineshopping5.entity.Order;
import com.example.onlinefigurineshopping5.entity.OrderItem;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(Long userId, Map<Long, Integer> figureIdWithQuantity,
                      String receiverName, String receiverPhone, String receiverAddress);
    Order findById(Long id);
    Order findByOrderNo(String orderNo);
    List<Order> findUserOrders(Long userId);
    List<Order> findAll();
    boolean payOrder(Long orderId);
    boolean cancelOrder(Long orderId);
    List<OrderItem> getOrderItems(Long orderId);
}