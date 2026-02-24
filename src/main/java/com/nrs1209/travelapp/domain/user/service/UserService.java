package com.nrs1209.travelapp.domain.user.service;

import com.nrs1209.travelapp.domain.user.dto.UserLoginRequestDTO;
import com.nrs1209.travelapp.domain.user.dto.UserResponseDTO;
import com.nrs1209.travelapp.domain.user.dto.UserSignUpRequestDTO;
import com.nrs1209.travelapp.domain.user.dto.UserUpdateRequestDTO;

public interface UserService {

    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
    public UserResponseDTO register(UserSignUpRequestDTO userSignUpRequestDTO);
    public UserResponseDTO modify (Long Id, UserUpdateRequestDTO userUpdateRequestDTO);

    public UserResponseDTO getUserById(Long userId);
}
