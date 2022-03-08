package com.github.dynolog.api.domain.user.service;

import java.util.List;

import com.github.dynolog.api.domain.role.repository.RoleRepository;
import com.github.dynolog.api.domain.session.model.TokenResponse;
import com.github.dynolog.api.domain.session.service.CreateTokenService;
import com.github.dynolog.api.domain.user.entity.User;
import com.github.dynolog.api.domain.user.form.CreateUserProps;
import com.github.dynolog.api.domain.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final CreateTokenService createTokenService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public CreateUserService(
        CreateTokenService createTokenService,
        RoleRepository roleRepository,
        UserRepository userRepository
    ) {
        this.createTokenService = createTokenService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public User create(CreateUserProps data) {
        
        data.validate();

        var roles = roleRepository.findOptionalByInitials("USER")
            .map(List::of)
                .orElseGet(List::of);

        return userRepository.save(new User(data, roles));
    }
    
    public TokenResponse createWithSession(CreateUserProps data) {
        var user = create(data);
        return createTokenService.create(user);
    }
}
