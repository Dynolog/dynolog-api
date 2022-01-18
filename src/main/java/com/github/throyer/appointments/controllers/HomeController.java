package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.utils.Hello;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HomeController {

    @ApiResponses(value = { @ApiResponse(description = "Redirect to \"/api\" path", responseCode = "302") })
    @RequestMapping(path = "/", method = GET)
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("api");
    }

    @RequestMapping(path =  "/api", method = GET)
    public Hello index() {
        return () -> "Is a live!";
    }
}
