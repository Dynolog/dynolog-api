package com.github.dynolog.api.internationalization;

import com.github.dynolog.api.utils.JSON;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Map.of;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class InternationalizationTests {

    public static final String PT_BR = "pt-BR";
    public static final String EN_US = "en-US";

    public static final String EN_US_MESSAGE = "Invalid password or username.";
    public static final String PT_BR_MESSAGE = "Senha ou usuário invalido.";

    public static final String MOCK_PASSWORD = "senha_bem_segura_1234";
    public static final String MOCK_EMAIL = "email@email.com";

    @Autowired
    private MockMvc api;

    @Test
    @DisplayName("Deve retornar as mensagens de erro em pt BR.")
    public void should_return_error_messages_in_pt_BR() throws Exception {
        var body = JSON.stringify(of(
            "password", MOCK_PASSWORD,
            "email", MOCK_EMAIL
        ));

        api.perform(post("/api/sessions")
                        .content(body)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, PT_BR))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(PT_BR_MESSAGE));;
    }

    @Test
    @DisplayName("Deve retornar as mensagens de erro em Inglês.")
    public void should_return_error_messages_in_english() throws Exception {
        var body = JSON.stringify(of(
            "password", MOCK_PASSWORD,
            "email", MOCK_EMAIL
        ));

        api.perform(post("/api/sessions")
                        .content(body)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_US))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(EN_US_MESSAGE));;
    }
}