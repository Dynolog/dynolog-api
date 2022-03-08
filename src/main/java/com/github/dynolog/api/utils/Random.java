package com.github.dynolog.api.utils;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.github.dynolog.api.utils.Constants.SECURITY.JWT;
import static com.github.dynolog.api.utils.Constants.SECURITY.TOKEN_SECRET;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.List.of;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import com.github.dynolog.api.domain.role.entity.Role;
import com.github.dynolog.api.domain.user.entity.User;
import com.github.javafaker.Faker;

public class Random {
        
    private static final java.util.Random RANDOM = new java.util.Random();
    public static final Faker FAKER = new Faker(new Locale("pt", "BR"), RANDOM);

    public static String password() {
        return FAKER.regexify("[a-z]{5,13}[1-9]{1,5}[A-Z]{1,5}[#?!@$%^&*-]{1,5}");
    }

    public static String name() {
        return  FAKER.name().fullName();
    }

    public static String email() {
        return FAKER.internet().safeEmailAddress();
    }

    public static String nanoid() {
        return randomNanoId();
    }

    public static User user() {
        return user(of());
    }

    public static User user(List<Role> roles) {
        return new User(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                password(),
                roles
        );
    }

    public static String token() {
        return token(now().plusHours(24), TOKEN_SECRET);
    }

    public static String token(String id, String roles) {
        return token(id, now().plusHours(24), TOKEN_SECRET, asList(roles.split(",")));
    }

    public static String token(List<String> roles) {
        return token(nanoid(), now().plusHours(24), TOKEN_SECRET, roles);
    }

    public static String token(LocalDateTime expiration) {
        return token(expiration, TOKEN_SECRET);
    }

    public static String token(LocalDateTime expiration, List<String> roles) {
        return token(nanoid(), expiration, TOKEN_SECRET, roles);
    }

    public static String token(LocalDateTime expiration, String secret) {
        return token(nanoid(), expiration, secret, of("ADM"));
    }

    public static String token(String id, LocalDateTime expiration, String secret, List<String> roles) {
        var token = JWT.encode(id, roles, expiration, secret);
        return format("Bearer %s", token);
    }
}