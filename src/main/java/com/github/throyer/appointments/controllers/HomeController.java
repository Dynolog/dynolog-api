package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.utils.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping
    public Hello index() {
        return () -> "Is a live!";
    }
}
