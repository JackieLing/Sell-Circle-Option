package com.ay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/test")
public class AyTestController {
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello";
    }
}
