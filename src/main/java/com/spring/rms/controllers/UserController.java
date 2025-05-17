package com.spring.rms.controllers;

import com.spring.rms.payload.request.RegisterDTO;
import com.spring.rms.payload.response.ApiResponse;
import com.spring.rms.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", null));
    }
}