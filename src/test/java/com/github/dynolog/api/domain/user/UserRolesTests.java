package com.github.dynolog.api.domain.user;

import com.github.dynolog.api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRolesTests {

    @Test
    @DisplayName("O usuario deve ter uma lista vazia de roles.")
    public void user_should_be_have_empty_list_of_roles() {
        var user = new User();
        assertTrue(user.getRoles().isEmpty());
    }
}
