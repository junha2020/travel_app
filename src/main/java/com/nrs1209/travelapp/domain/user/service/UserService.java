package com.nrs1209.travelapp.domain.user.service;

import com.nrs1209.travelapp.domain.user.dto.*;

public interface UserService {

    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
    public UserResponseDTO register(UserSignUpRequestDTO userSignUpRequestDTO);
    public UserResponseDTO modify (Long Id, UserUpdateRequestDTO userUpdateRequestDTO);

    public UserResponseDTO getUserById(Long userId);

    FindUsernameResponseDTO findUsername(FindUsernameRequestDTO requestDTO);
    ResetPasswordResponseDTO resetPassword(ResetPasswordRequestDTO requestDTO);
}
