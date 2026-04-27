package com.example.onlinefigurineshopping5.controller;

import com.example.onlinefigurineshopping5.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public Result<String> sayHello() {
        return Result.success("Hello Spring Boot!");
    }

    @GetMapping("/{name}")
    public Result<String> sayHelloTo(@PathVariable String name) {
        return Result.success("Hello " + name + "!");
    }
}