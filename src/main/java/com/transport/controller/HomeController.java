package com.transport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@RestController
public class HomeController {
    
    @GetMapping ("/")
    public String home() {
        return "WELCOME TO TRANSPORT SYSTEM API!";
    }
}
