package com.spring.rms.services;


import com.spring.rms.payload.request.RegisterDTO;
import com.spring.rms.payload.request.LoginDTO;

public interface IAuthService {
    String login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
}