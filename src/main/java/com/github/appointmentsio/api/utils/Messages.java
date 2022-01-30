package com.github.appointmentsio.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    public static ResourceBundleMessageSource messageSource;

    @Autowired
    public Messages(ResourceBundleMessageSource resourceBundleMessageSource) {
        Messages.messageSource = resourceBundleMessageSource;
    }

    public static String message(String field) {
        return messageSource.getMessage(field, null, LocaleContextHolder.getLocale());
    }

    public static String message(String field, Object... args) {
        return messageSource.getMessage(field, args, LocaleContextHolder.getLocale());
    }
}
