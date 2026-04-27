package com.example.onlinefigurineshopping5.entity;

import java.math.BigDecimal;

/**
 * 订单明细实体类
 */
public class OrderItem {
    private Long id;              // 明细ID
    private Long orderId;         // 订单ID
    private Long figureId;        // 手办ID
    private String figureName;    // 手办名称（冗余存储）
    private BigDecimal price;     // 购买时单价
    private Integer quantity;     // 购买数量
    private BigDecimal totalPrice; // 小计

    // 无参构造
    public OrderItem() {
    }

    // 创建订单明细时的构造方法
    public OrderItem(Long orderId, Long figureId, String figureName,
                     BigDecimal price, Integer quantity) {
        this.orderId = orderId;
        this.figureId = figureId;
        this.figureName = figureName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getFigureId() {
        return figureId;
    }

    public void setFigureId(Long figureId) {
        this.figureId = figureId;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", figureName='" + figureName + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}