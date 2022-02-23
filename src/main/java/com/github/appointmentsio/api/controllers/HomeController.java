package com.github.appointmentsio.api.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.github.appointmentsio.api.utils.Hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class HomeController {

    @Operation(hidden = true)
    @RequestMapping(path = "/", method = GET)
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("documentation");
    }

    @Operation(hidden = true)
    @RequestMapping(path =  "/api", method = GET)
    public Hello index() {
        return () -> "Is a live!";
    }
}
