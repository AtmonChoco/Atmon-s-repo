package com.example.onlinefigurineshopping5.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手办实体类
 */
public class Figure {
    private Long id;              // 手办ID
    private String name;          // 手办名称
    private BigDecimal price;     // 价格
    private Integer stock;        // 库存
    private String category;      // 分类
    private String brand;         // 品牌
    private String description;   // 描述
    private String imageUrl;      // 图片地址
    private Integer status;       // 状态：0下架/1上架
    private Date createTime;      // 上架时间

    // 无参构造
    public Figure() {
    }

    // 有参构造（添加手办时使用）
    public Figure(String name, BigDecimal price, Integer stock, String category,
                  String brand, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = 1;  // 默认上架
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }
}