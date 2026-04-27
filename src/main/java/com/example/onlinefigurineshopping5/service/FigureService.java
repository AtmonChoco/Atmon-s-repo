package com.example.onlinefigurineshopping5.service;

import com.example.onlinefigurineshopping5.entity.Figure;
import java.util.List;

public interface FigureService {
    Figure addFigure(Figure figure);
    Figure findById(Long id);
    List<Figure> findAllOnSale();
    List<Figure> findAll();
    List<Figure> findByCategory(String category);
    List<Figure> search(String keyword);
    boolean reduceStock(Long id, Integer quantity);

    boolean addStock(Long id, Integer quantity);

    Figure updateFigure(Figure figure);
    boolean updateStatus(Long id, Integer status);
}