package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.utils.Hello;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping(path = "/", method = GET)
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("api");
    }

    @RequestMapping(path =  "/api", method = GET)
    public Hello index() {
        return () -> "Is a live!";
    }
}
