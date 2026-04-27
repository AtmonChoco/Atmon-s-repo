package com.example.onlinefigurineshopping5.mapper;

import com.example.onlinefigurineshopping5.entity.Figure;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FigureMapper {

    @Insert("INSERT INTO figure(name, price, stock, category, brand, description, image_url, status) " +
            "VALUES(#{name}, #{price}, #{stock}, #{category}, #{brand}, #{description}, #{imageUrl}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Figure figure);

    @Select("SELECT * FROM figure WHERE id = #{id}")
    Figure findById(Long id);

    @Select("SELECT * FROM figure WHERE status = 1")
    List<Figure> findAllOnSale();

    @Select("SELECT * FROM figure")
    List<Figure> findAll();

    @Select("SELECT * FROM figure WHERE category = #{category} AND status = 1")
    List<Figure> findByCategory(String category);

    @Select("SELECT * FROM figure WHERE name LIKE CONCAT('%', #{keyword}, '%') AND status = 1")
    List<Figure> searchByName(String keyword);

    @Update("UPDATE figure SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int reduceStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE figure SET stock = stock + #{quantity} WHERE id = #{id}")
    int addStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE figure SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE figure SET name = #{name}, price = #{price}, stock = #{stock}, " +
            "category = #{category}, brand = #{brand}, description = #{description}, image_url = #{imageUrl} " +
            "WHERE id = #{id}")
    int update(Figure figure);
}