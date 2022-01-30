package com.github.appointmentsio.api.utils;

import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;

public class Random {
        
    private static final java.util.Random RANDOM = new java.util.Random();
    public static final Faker FAKER = new Faker(new Locale("pt", "BR"), RANDOM);

    public static Integer between(Integer min, Integer max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static <T> T getRandomElement(List<T> itens) {
        return itens.get(RANDOM.nextInt(itens.size()));
    }

    public static String code() {
        return String.format("%s%s%s%s", between(0, 9), between(0, 9), between(0, 9), between(0, 9));
    }

    public static String password() {
        return FAKER.regexify("[a-z]{5,13}[1-9]{1,5}[A-Z]{1,5}[#?!@$%^&*-]{1,5}");
    }
}