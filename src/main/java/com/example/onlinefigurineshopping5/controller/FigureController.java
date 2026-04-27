package com.example.onlinefigurineshopping5.controller;

import com.example.onlinefigurineshopping5.common.Result;
import com.example.onlinefigurineshopping5.entity.Figure;
import com.example.onlinefigurineshopping5.service.FigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/figure")
public class FigureController {

    @Autowired
    private FigureService figureService;

    // 获取所有在售手办
    @GetMapping("/list")
    public Result<List<Figure>> listOnSale() {
        List<Figure> figures = figureService.findAllOnSale();
        return Result.success(figures);
    }

    // 获取所有手办（管理员用）
    @GetMapping("/all")
    public Result<List<Figure>> listAll() {
        List<Figure> figures = figureService.findAll();
        return Result.success(figures);
    }

    // 根据分类获取手办
    @GetMapping("/category/{category}")
    public Result<List<Figure>> listByCategory(@PathVariable String category) {
        List<Figure> figures = figureService.findByCategory(category);
        return Result.success(figures);
    }

    // 搜索手办
    @GetMapping("/search")
    public Result<List<Figure>> search(@RequestParam(required = false) String keyword) {
        List<Figure> figures = figureService.search(keyword);
        return Result.success(figures);
    }

    // 获取手办详情
    @GetMapping("/{id}")
    public Result<Figure> getDetail(@PathVariable Long id) {
        Figure figure = figureService.findById(id);
        if (figure == null) {
            return Result.error("手办不存在");
        }
        return Result.success(figure);
    }

    // 添加手办（管理员）
    @PostMapping("/add")
    public Result<Figure> addFigure(@RequestBody Figure figure) {
        try {
            Figure newFigure = figureService.addFigure(figure);
            return Result.success(newFigure);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 更新手办信息（管理员）
    @PutMapping("/update")
    public Result<Figure> updateFigure(@RequestBody Figure figure) {
        try {
            Figure updated = figureService.updateFigure(figure);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 上下架手办（管理员）
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        boolean success = figureService.updateStatus(id, status);
        if (success) {
            return Result.success(status == 1 ? "已上架" : "已下架");
        } else {
            return Result.error("操作失败");
        }
    }
}