package com.github.throyer.appointments.domain.user.service;

import com.github.throyer.appointments.domain.role.repository.RoleRepository;
import com.github.throyer.appointments.domain.session.service.CreateTokenService;
import com.github.throyer.appointments.domain.user.dto.CreateUserData;
import com.github.throyer.appointments.domain.session.dto.TokenResponse;
import com.github.throyer.appointments.domain.user.dto.UserDetails;
import com.github.throyer.appointments.domain.user.entity.User;
import com.github.throyer.appointments.domain.user.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private CreateTokenService createTokenService;
    
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
        
    public UserDetails create(CreateUserData data) {
        
        data.validate();

        var roles = roleRepository.findOptionalByInitials("USER")
            .map(role -> List.of(role))
                .orElseGet(() -> List.of());
        
        var user = userRepository.save(new User(data, roles));

        return new UserDetails(user);
    }
    
    public TokenResponse createWithSession(CreateUserData data) {
        var user = create(data);
        var token = createTokenService.create(user);
        return token;
    }
}
