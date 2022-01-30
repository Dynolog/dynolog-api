package com.github.appointmentsio.api.domain.user.service;

import com.github.appointmentsio.api.domain.role.repository.RoleRepository;
import com.github.appointmentsio.api.domain.session.model.TokenResponse;
import com.github.appointmentsio.api.domain.session.service.CreateTokenService;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.form.CreateUserProps;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
