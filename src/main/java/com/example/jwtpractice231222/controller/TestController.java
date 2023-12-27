package com.example.jwtpractice231222.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("ok");
    }
}
