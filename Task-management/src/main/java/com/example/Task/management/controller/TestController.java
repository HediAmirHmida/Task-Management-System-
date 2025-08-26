package com.example.Task.management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public Map<String, Object> testDatabase() {
        try {
            String result = jdbcTemplate.queryForObject("SELECT 'Database connected!' as message", String.class);
            return Map.of("status", "SUCCESS", "message", result, "timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            return Map.of("status", "ERROR", "message", e.getMessage(), "timestamp", System.currentTimeMillis());
        }
    }
}
