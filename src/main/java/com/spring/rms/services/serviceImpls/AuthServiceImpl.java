package com.spring.rms.services.serviceImpls;

import com.spring.rms.enums.ERole;
import com.spring.rms.payload.request.RegisterDTO;
import com.spring.rms.models.Role;
import com.spring.rms.models.User;
import com.spring.rms.payload.request.LoginDTO;
import com.spring.rms.repositories.IRoleRepository;
import com.spring.rms.repositories.IUserRepository;
import com.spring.rms.security.JwtTokenProvider;
import com.spring.rms.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent() ||
                userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());

        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRoles(new HashSet<>());
        user.getRoles().add(userRole);

        userRepository.save(user);
    }
}