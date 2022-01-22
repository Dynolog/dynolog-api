package com.github.throyer.appointments.utils;

import com.github.throyer.appointments.domain.shared.Identity;
import com.github.throyer.appointments.errors.Error;
import static com.github.throyer.appointments.utils.JsonUtils.toJson;
import static java.lang.String.format;
import static java.net.URI.create;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class Response {

    private static final Logger LOGGER = Logger.getLogger(Response.class.getName());

    private Response() { }

    public static void forbidden(HttpServletResponse response) {
        try {
            response.setStatus(FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(toJson(
                    new Error("Can't find token on Authorization header.", FORBIDDEN)
            ));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "can't write response error on token expired or invalid exception", exception);
        }
    }

    public static final ResponseEntity<Error> fromException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                    .body(new Error(exception.getReason(), exception.getStatus()));
    }

    public static final <T> ResponseEntity<T> forbidden(T body) {
        return ResponseEntity.status(403).body(body);
    }

    public static final <T> ResponseEntity<T> forbidden() {
        return ResponseEntity.status(403).build();
    }

    public static final <T> ResponseEntity<T> unauthorized(T body) {
        return ResponseEntity.status(401).body(body);
    }

    public static final <T> ResponseEntity<T> unauthorized() {
        return ResponseEntity.status(401).build();
    }

    public static final <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static final <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok()
                .build();
    }

    public static final <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound()
                .build();
    }

    public static final <T> ResponseEntity<T> badRequest(T body) {
        return ResponseEntity.badRequest()
                .body(body);
    }

    public static final <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest()
                .build();
    }

    public static final <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static final <T> ResponseEntity<T> noContent(T entity, CrudRepository<T, ?> repository) {
        repository.delete(entity);
        return ResponseEntity
                .noContent()
                .build();
    }

    public static final <T extends Identity> ResponseEntity<T> created(T entity, String location) {
        return ResponseEntity.created(create(format("/%s/%s", location, entity.getId())))
                .body(entity);
    }

    public static final <T> ResponseEntity<T> created(T body, String location, Long id) {
        return ResponseEntity.created(create(format("/%s/%s", location, id)))
                .body(body);
    }

    public static final <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(CREATED)
                .body(body);
    }

    public static final ResponseStatusException forbidden(String reason) {
        return new ResponseStatusException(FORBIDDEN, reason);
    }

    public static final ResponseStatusException unauthorized(String reason) {
        return new ResponseStatusException(UNAUTHORIZED, reason);
    }

    public static final ResponseStatusException notFound(String reason) {
        return new ResponseStatusException(NOT_FOUND, reason);
    }

    public static final ResponseStatusException internalServerError(String reason) {
        return new ResponseStatusException(INTERNAL_SERVER_ERROR, reason);
    }
}