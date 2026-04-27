package com.example.onlinefigurineshopping5.service.impl;

import com.example.onlinefigurineshopping5.entity.Figure;
import com.example.onlinefigurineshopping5.entity.Order;
import com.example.onlinefigurineshopping5.entity.OrderItem;
import com.example.onlinefigurineshopping5.mapper.OrderItemMapper;
import com.example.onlinefigurineshopping5.mapper.OrderMapper;
import com.example.onlinefigurineshopping5.service.FigureService;
import com.example.onlinefigurineshopping5.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private FigureService figureService;

    // 生成订单编号
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "FIG" + sdf.format(new Date()) + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Map<Long, Integer> figureIdWithQuantity,
                             String receiverName, String receiverPhone, String receiverAddress) {

        List<OrderItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 检查库存并计算总金额
        for (Map.Entry<Long, Integer> entry : figureIdWithQuantity.entrySet()) {
            Long figureId = entry.getKey();
            Integer quantity = entry.getValue();

            Figure figure = figureService.findById(figureId);
            if (figure == null) {
                throw new RuntimeException("手办不存在: " + figureId);
            }
            if (figure.getStock() < quantity) {
                throw new RuntimeException("库存不足: " + figure.getName());
            }

            // 扣减库存
            figureService.reduceStock(figureId, quantity);

            // 创建订单明细
            OrderItem item = new OrderItem();
            item.setFigureId(figureId);
            item.setFigureName(figure.getName());
            item.setPrice(figure.getPrice());
            item.setQuantity(quantity);
            item.setTotalPrice(figure.getPrice().multiply(BigDecimal.valueOf(quantity)));

            totalAmount = totalAmount.add(item.getTotalPrice());
            items.add(item);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.STATUS_WAIT_PAY);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);

        orderMapper.insert(order);

        // 保存订单明细
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        return order;
    }

    @Override
    public Order findById(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public List<Order> findUserOrders(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    public List<Order> findAll() {
        return orderMapper.findAll();
    }

    @Override
    @Transactional
    public boolean payOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != Order.STATUS_WAIT_PAY) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }
        int result = orderMapper.pay(orderId, Order.STATUS_PAID);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != Order.STATUS_WAIT_PAY) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        // 恢复库存
        List<OrderItem> items = orderItemMapper.findByOrderId(orderId);
        for (OrderItem item : items) {
            figureService.addStock(item.getFigureId(), item.getQuantity());
        }
        int result = orderMapper.updateStatus(orderId, Order.STATUS_CANCELLED);
        return result > 0;
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }
}