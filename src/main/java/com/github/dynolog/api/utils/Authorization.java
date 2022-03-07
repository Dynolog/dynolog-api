package com.github.dynolog.api.utils;

import static com.github.dynolog.api.utils.Constants.SECURITY.ACCEPTABLE_TOKEN_TYPE;
import static com.github.dynolog.api.utils.Constants.SECURITY.AUTHORIZATION_HEADER;
import static com.github.dynolog.api.utils.Constants.SECURITY.SECURITY_TYPE;
import static java.util.Objects.isNull;

import javax.servlet.http.HttpServletRequest;

public class Authorization {
    private Authorization() { }

    public static String extract(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token)) {
            return null;
        }

        if (token.substring(7).equals(SECURITY_TYPE)) {
            return null;
        }

        return token.substring(7);
    }

    public static boolean tokenIsNull(String token) {
        return isNull(token) || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
    }
}
