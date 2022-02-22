package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.service.CreateProjectService;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.form.CreateUserProps;
import com.github.appointmentsio.api.domain.user.service.CreateUserService;
import com.github.appointmentsio.api.utils.JSON;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static com.github.appointmentsio.api.utils.Random.*;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = MOCK)
public class TimeEntryControllerTests {

    private static final String TIME_ENTRIES_URL = "/api/time-entries";
    private static final String USER_ROLE = "USER";

    @Autowired
    private MockMvc api;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private CreateProjectService createProjectService;

    private User user;
    private Project project;

    @BeforeAll
    public void setup() {
        user = createUserService.create(new CreateUserProps(
                name(),
                email(),
                password()
        ));

        project = createProjectService.create(new CreateProjectProps(
                "fake_project",
                BigDecimal.valueOf(60L),
                "BRL", user.getNanoId()
        ));
    }

    @Test
    @DisplayName("não deve listar time entries com um intervalo maior que seis meses")
    public void must_not_list_time_entries_more_than_six_months() throws Exception {
        var now = now();
        var startDate = now.format(ISO_LOCAL_DATE_TIME);
        var endDate = now.plusMonths(7).format(ISO_LOCAL_DATE_TIME);

        api.perform(get(TIME_ENTRIES_URL)
                        .header(AUTHORIZATION, token(user.getNanoId(), USER_ROLE))
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .queryParam("userId", user.getNanoId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.message == 'The interval cannot be longer than 6 months.')]", hasSize(1)));
    }

    @Test
    @DisplayName("deve listar time entries com um intervalo menor que seis meses")
    public void must_list_time_entries_with_interval_less_than_six_months() throws Exception {
        var now = now();
        var startDate = now.format(ISO_LOCAL_DATE_TIME);
        var endDate = now.plusMonths(2).format(ISO_LOCAL_DATE_TIME);

        api.perform(get(TIME_ENTRIES_URL)
                        .header(AUTHORIZATION, token(user.getNanoId(), USER_ROLE))
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .queryParam("userId", user.getNanoId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("não deve cadastrar um time entry sem os campos requeridos")
    public void must_not_register_time_entry_without_the_required_fields() throws Exception {
        var now = now();
        var endDate = now.plusMonths(2).format(ISO_LOCAL_DATE_TIME);

        var body = JSON.stringify(Map.of(
                "stop", endDate,
                "projectId", project.getNanoId()
        ));

        api.perform(post(TIME_ENTRIES_URL)
                        .header(AUTHORIZATION, token(user.getNanoId(), USER_ROLE))
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.message == 'User id is a required field.')]", hasSize(1)))
                .andExpect(jsonPath("$.errors[?(@.message == 'Start is a required field.')]", hasSize(1)));
    }
}
