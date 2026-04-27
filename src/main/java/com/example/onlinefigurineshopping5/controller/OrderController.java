package com.example.onlinefigurineshopping5.controller;

import com.example.onlinefigurineshopping5.common.Result;
import com.example.onlinefigurineshopping5.entity.Order;
import com.example.onlinefigurineshopping5.entity.OrderItem;
import com.example.onlinefigurineshopping5.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            String receiverName = (String) params.get("receiverName");
            String receiverPhone = (String) params.get("receiverPhone");
            String receiverAddress = (String) params.get("receiverAddress");

            // 获取购物车商品: {figureId: quantity}
            Map<String, Integer> cartMap = (Map<String, Integer>) params.get("cart");
            Map<Long, Integer> figureIdWithQuantity = new java.util.HashMap<>();
            for (Map.Entry<String, Integer> entry : cartMap.entrySet()) {
                figureIdWithQuantity.put(Long.valueOf(entry.getKey()), entry.getValue());
            }

            Order order = orderService.createOrder(userId, figureIdWithQuantity,
                    receiverName, receiverPhone, receiverAddress);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 支付订单
    @PutMapping("/pay/{orderId}")
    public Result<String> payOrder(@PathVariable Long orderId) {
        try {
            boolean success = orderService.payOrder(orderId);
            if (success) {
                return Result.success("支付成功");
            } else {
                return Result.error("支付失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 取消订单
    @PutMapping("/cancel/{orderId}")
    public Result<String> cancelOrder(@PathVariable Long orderId) {
        try {
            boolean success = orderService.cancelOrder(orderId);
            if (success) {
                return Result.success("订单已取消");
            } else {
                return Result.error("取消失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 获取订单详情
    @GetMapping("/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    // 获取订单明细
    @GetMapping("/items/{orderId}")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return Result.success(items);
    }

    // 获取用户的所有订单
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.findUserOrders(userId);
        return Result.success(orders);
    }

    // 获取所有订单（管理员）
    @GetMapping("/list")
    public Result<List<Order>> listAllOrders() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }
}