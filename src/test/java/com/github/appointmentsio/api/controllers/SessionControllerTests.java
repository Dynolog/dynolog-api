package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.utils.JSON;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.github.appointmentsio.api.utils.Random.token;
import static com.github.appointmentsio.api.utils.Random.user;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
public class SessionControllerTests {
    @Autowired
    private MockMvc api;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Deve retornar OK quando a senha estiver correta.")
    public void should_return_OK_when_password_is_correct() throws Exception {

        var user = user();

        var email = user.getEmail();
        var password = user.getPassword();

        repository.save(user);

        var body = """
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """;

        var request = post("/api/sessions")
                .content(format(body, email, password))
                .header(CONTENT_TYPE, APPLICATION_JSON);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar FORBIDDEN quando a senha estiver incorreta.")
    public void should_return_FORBIDDEN_when_password_is_incorrect() throws Exception {
        var user = repository.save(user());

        var body = JSON.stringify(Map.of(
                "email", user.getEmail(),
                "password", "Írineu! você não sabe, nem eu!"
        ));

        var request = post("/api/sessions")
                .content(body)
                .header(CONTENT_TYPE, APPLICATION_JSON);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar FORBIDDEN quando o usuário não existir.")
    public void should_return_FORBIDDEN_when_user_does_not_exist() throws Exception {
        var body = JSON.stringify(Map.of(
                "email", "this.not.exist@email.com",
                "password", "Írineu! você não sabe, nem eu!"
        ));

        var request = post("/api/sessions")
                .content(body)
                .header(CONTENT_TYPE, APPLICATION_JSON);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Não deve aceitar requisições sem o token no cabeçalho quando as rotas forem privadas.")
    public void should_not_accept_requests_without_token_in_header_when_routes_are_private() throws Exception {
        var request = get("/api/users");

        api.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Can't find token on Authorization header."));
    }

    @Test
    @DisplayName("Não deve aceitar requisições com o token expirado.")
    public void should_not_accept_requests_with_token_expired() throws Exception {
        var expiredToken = token(now().minusHours(24));

        var request = get("/api/users").header(AUTHORIZATION, expiredToken);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Token expired or invalid."));
    }

    @Test
    @DisplayName("Deve aceitar requisições com o token um válido.")
    public void should_accept_requests_with_token_valid() throws Exception {
        var token = token(of("USER"));
        var request = get("/api/timezones").header(AUTHORIZATION, token);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Não deve aceitar requisições com o token um inválido.")
    public void must_not_accept_requests_with_an_invalid_token() throws Exception {
        var token = token(now().plusHours(24), "this_is_not_my_secret");
        var request = get("/api/timezones").header(AUTHORIZATION, token);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Token expired or invalid."));
    }

    @Test
    @DisplayName("Não deve aceitar requisições com o token sem a role correta.")
    public void must_not_accept_requests_with_token_without_the_correct_role() throws Exception {
        var token = token(of("THIS_IS_NOT_CORRECT_ROLE"));
        var request = get("/api/timezones").header(AUTHORIZATION, token);

        api.perform(request)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Not authorized."));
    }

    @Test
    @DisplayName("Deve aceitar requisições sem token em rotas publicas.")
    public void temp() throws Exception {
        var request = get("/api");

        api.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Is a live!"));
    }
}
