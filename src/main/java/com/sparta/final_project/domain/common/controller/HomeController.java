package com.sparta.final_project.domain.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/health")
    public String health() {
        return "ok!";
    }
}
