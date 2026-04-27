package com.example.onlinefigurineshopping5.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 */
public class Order {
    private Long id;                 // 订单ID
    private String orderNo;          // 订单编号
    private Long userId;             // 用户ID
    private BigDecimal totalAmount;  // 订单总金额
    private Integer status;          // 订单状态
    private String receiverName;     // 收货人
    private String receiverPhone;    // 收货电话
    private String receiverAddress;  // 收货地址
    private Date createTime;         // 下单时间
    private Date payTime;            // 付款时间

    // 订单状态常量
    public static final int STATUS_WAIT_PAY = 0;      // 待付款
    public static final int STATUS_PAID = 1;          // 已付款
    public static final int STATUS_SHIPPED = 2;       // 已发货
    public static final int STATUS_COMPLETED = 3;     // 已完成
    public static final int STATUS_CANCELLED = 4;     // 已取消

    // 无参构造
    public Order() {
    }

    // 创建订单时的构造方法
    public Order(String orderNo, Long userId, BigDecimal totalAmount,
                 String receiverName, String receiverPhone, String receiverAddress) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.status = STATUS_WAIT_PAY;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    // 辅助方法：获取状态文本
    public String getStatusText() {
        switch (status) {
            case STATUS_WAIT_PAY: return "待付款";
            case STATUS_PAID: return "已付款";
            case STATUS_SHIPPED: return "已发货";
            case STATUS_COMPLETED: return "已完成";
            case STATUS_CANCELLED: return "已取消";
            default: return "未知";
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + getStatusText() +
                '}';
    }
}