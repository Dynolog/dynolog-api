package com.github.throyer.appointments.configurations;

import com.github.throyer.appointments.domain.session.service.SessionService;
import com.github.throyer.appointments.middlewares.AuthorizationMiddleware;
import static com.github.throyer.appointments.utils.Constraints.PASSWORD_ENCODER;
import static com.github.throyer.appointments.utils.Response.forbidden;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

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
import org.springframework.web.cors.CorsConfiguration;

@Component
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    public static final String[] STATIC_FILES = {
        "/robots.txt",
        "/font/**",
        "/css/**",
        "/webjars/**",
        "/webjars/",
        "/js/**",
        "/favicon.ico",
        "/**.html",
        "/documentation/**"
    };   

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AuthorizationMiddleware authorizationMiddleware;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sessionService)
            .passwordEncoder(PASSWORD_ENCODER);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers(GET, "/api")
                        .permitAll()
                    .antMatchers(POST, "/api/users")
                        .permitAll()
                    .antMatchers(POST, "/api/sessions", "/api/sessions/refresh")
                        .permitAll()
                    .antMatchers(GET, "/documentation/**", "/swagger-ui/**")
                        .permitAll()
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
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());      
            
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(STATIC_FILES);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
