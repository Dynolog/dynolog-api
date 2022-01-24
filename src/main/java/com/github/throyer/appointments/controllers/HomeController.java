package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.utils.Hello;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.throyer.appointments.utils.JsonUtils.toJson;
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
