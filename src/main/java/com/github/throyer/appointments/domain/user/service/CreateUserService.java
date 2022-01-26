package com.github.throyer.appointments.domain.user.service;

import com.github.throyer.appointments.domain.role.repository.RoleRepository;
import com.github.throyer.appointments.domain.session.dto.TokenResponse;
import com.github.throyer.appointments.domain.session.service.CreateTokenService;
import com.github.throyer.appointments.domain.user.entity.User;
import com.github.throyer.appointments.domain.user.model.CreateUserProps;
import com.github.throyer.appointments.domain.user.model.SimplifiedUser;
import com.github.throyer.appointments.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    public SimplifiedUser create(CreateUserProps data) {
        
        data.validate();

        var roles = roleRepository.findOptionalByInitials("USER")
            .map(List::of)
                .orElseGet(List::of);
        
        var user = userRepository.save(new User(data, roles));

        return new SimplifiedUser(user);
    }
    
    public TokenResponse createWithSession(CreateUserProps data) {
        var user = create(data);
        return createTokenService.create(user);
    }
}
