package com.github.appointmentsio.api.configurations;

import com.github.appointmentsio.api.domain.session.service.SessionService;
import com.github.appointmentsio.api.middlewares.AuthorizationMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import static com.github.appointmentsio.api.utils.Constants.SECURITY.*;
import static com.github.appointmentsio.api.utils.Response.forbidden;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Component
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AuthorizationMiddleware authorizationMiddleware;

    @Override
    protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.userDetailsService(sessionService)
                .passwordEncoder(PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        PUBLIC_ROUTES.injectOn(security);

        security
            .antMatcher("/**")
                .authorizeRequests()
                    .anyRequest()
                        .authenticated()
            .and()
                .csrf()
                    .disable()
                        .exceptionHandling()
                            .authenticationEntryPoint((request, response, exception) -> forbidden(response))
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(STATELESS)
            .and()
                .addFilterBefore(authorizationMiddleware, UsernamePasswordAuthenticationFilter.class)
                    .cors()
                        .configurationSource(request -> CORS_CONFIGURATION.applyPermitDefaultValues());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(STATIC_FILES);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
