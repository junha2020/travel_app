package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.UserLoginRequestDTO;
import com.example.demo.domain.user.dto.UserResponseDTO;
import com.example.demo.domain.user.dto.UserSignUpRequestDTO;
import com.example.demo.domain.user.entity.User;

public interface UserService {

    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
    public UserResponseDTO register(UserSignUpRequestDTO userSignUpRequestDTO);
    public UserResponseDTO modify (String Username, String Password);

}
