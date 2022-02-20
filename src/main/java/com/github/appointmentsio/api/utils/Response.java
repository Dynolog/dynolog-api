package com.github.appointmentsio.api.utils;

import com.github.appointmentsio.api.errors.Error;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TOKEN_EXPIRED_OR_INVALID;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TOKEN_HEADER_MISSING_MESSAGE;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.CAN_T_WRITE_RESPONSE_ERROR;
import static com.github.appointmentsio.api.utils.JSON.stringify;
import static com.github.appointmentsio.api.utils.Messages.message;
import static java.lang.String.format;
import static java.net.URI.create;
import static org.springframework.http.HttpStatus.*;

public class Response {

    private static final Logger LOGGER = Logger.getLogger(Response.class.getName());

    private Response() { }

    public static void forbidden(HttpServletResponse response) {
        if (response.isCommitted()) {
            return;
        }

        try {
            response.setStatus(FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(stringify(
                    new Error(message(TOKEN_HEADER_MISSING_MESSAGE), FORBIDDEN)
            ));
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, CAN_T_WRITE_RESPONSE_ERROR, exception);
        }
    }

    public static void expired(HttpServletResponse response) {
        try {
            response.setStatus(FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(stringify(
                    new Error(message(TOKEN_EXPIRED_OR_INVALID), FORBIDDEN)
            ));
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, CAN_T_WRITE_RESPONSE_ERROR, exception);
        }
    }

    public static ResponseEntity<Error> fromException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                    .body(new Error(exception.getReason(), exception.getStatus()));
    }

    public static <T> ResponseEntity<T> forbidden(T body) {
        return ResponseEntity.status(403).body(body);
    }

    public static <T> ResponseEntity<T> forbidden() {
        return ResponseEntity.status(403).build();
    }

    public static <T> ResponseEntity<T> unauthorized(T body) {
        return ResponseEntity.status(401).body(body);
    }

    public static <T> ResponseEntity<T> unauthorized() {
        return ResponseEntity.status(401).build();
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok()
                .build();
    }

    public static <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound()
                .build();
    }

    public static <T> ResponseEntity<T> badRequest(T body) {
        return ResponseEntity.badRequest()
                .body(body);
    }

    public static <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest()
                .build();
    }

    public static <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static <T> ResponseEntity<T> noContent(T entity, CrudRepository<T, ?> repository) {
        repository.delete(entity);
        return ResponseEntity
                .noContent()
                .build();
    }

    public static <T> ResponseEntity<T> created(T body, String location, Object id) {
        return ResponseEntity.created(create(format("/%s/%s", location, id)))
                .body(body);
    }

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(CREATED)
                .body(body);
    }

    public static ResponseStatusException forbidden(String reason) {
        return new ResponseStatusException(FORBIDDEN, reason);
    }

    public static ResponseStatusException unauthorized(String reason) {
        return new ResponseStatusException(UNAUTHORIZED, reason);
    }

    public static ResponseStatusException notFound(String reason) {
        return new ResponseStatusException(NOT_FOUND, reason);
    }

    public static ResponseStatusException internalServerError(String reason) {
        return new ResponseStatusException(INTERNAL_SERVER_ERROR, reason);
    }
}
