package com.github.dynolog.api.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Appointments.io API",
    version = "1.0.0",
    contact = @Contact(
        name = "Throyer",
        email = "throyer.dev@gmail.com",
        url = "https://github.com/Throyer"
    )
))
@SecurityScheme(
    name = "token",
    type = HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER,
    scheme = "bearer"
)
public class SpringWebConfiguration implements WebMvcConfigurer {
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            var acceptLanguage = new Parameter()
                .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name("Accept-Language")
                    .description("Informs the client's language")
                    .required(false);

            operation.addParametersItem(acceptLanguage);
            return operation;
        };
    }
}
