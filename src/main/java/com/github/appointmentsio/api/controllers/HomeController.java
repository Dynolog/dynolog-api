package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.utils.Hello;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
