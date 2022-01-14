package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.utils.Hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/api")
    public Hello index() {
        return () -> "Is a live!";
    }
}
