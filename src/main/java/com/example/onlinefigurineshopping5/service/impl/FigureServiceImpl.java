package com.example.onlinefigurineshopping5.service.impl;

import com.example.onlinefigurineshopping5.entity.Figure;
import com.example.onlinefigurineshopping5.mapper.FigureMapper;
import com.example.onlinefigurineshopping5.service.FigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FigureServiceImpl implements FigureService {

    @Autowired
    private FigureMapper figureMapper;

    @Override
    public Figure addFigure(Figure figure) {
        figureMapper.insert(figure);
        return figure;
    }

    @Override
    public Figure findById(Long id) {
        return figureMapper.findById(id);
    }

    @Override
    public List<Figure> findAllOnSale() {
        return figureMapper.findAllOnSale();
    }

    @Override
    public List<Figure> findAll() {
        return figureMapper.findAll();
    }

    @Override
    public List<Figure> findByCategory(String category) {
        return figureMapper.findByCategory(category);
    }

    @Override
    public List<Figure> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllOnSale();
        }
        return figureMapper.searchByName(keyword);
    }

    @Override
    public boolean reduceStock(Long id, Integer quantity) {
        int result = figureMapper.reduceStock(id, quantity);
        return result > 0;
    }

    @Override
    public boolean addStock(Long id,Integer quantity) {
        int result = figureMapper.addStock(id, quantity);
        return result > 0;
    }

    @Override
    public Figure updateFigure(Figure figure) {
        figureMapper.update(figure);
        return figureMapper.findById(figure.getId());
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        int result = figureMapper.updateStatus(id, status);
        return result > 0;
    }
}