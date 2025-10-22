package com.example.demo.domain.user.service;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.user.dto.UserLoginRequestDTO;
import com.example.demo.domain.user.dto.UserResponseDTO;
import com.example.demo.domain.user.dto.UserSignUpRequestDTO;
import com.example.demo.domain.user.dto.UserUpdateRequestDTO;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userRepository.findByUserName(userLoginRequestDTO.getUserName())
            .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return new UserResponseDTO(user.getId(), user.getUserName(), user.getEmail(), user.getNickName());
    }

    @Override
    @Transactional
    public UserResponseDTO register(UserSignUpRequestDTO userSignUpRequestDTO) {
        if (userRepository.findByUserName(userSignUpRequestDTO.getUserName()).isPresent()) {
           throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        if (userRepository.findByEmail(userSignUpRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequestDTO.getPassword());

        User user = User.builder()
                .userName(userSignUpRequestDTO.getUserName())
                .password(encodedPassword)
                .email(userSignUpRequestDTO.getEmail())
                .nickName(userSignUpRequestDTO.getNickName())
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUserName(), savedUser.getEmail(), savedUser.getNickName());

    }

    @Override
    @Transactional
    public UserResponseDTO modify (Long Id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (userUpdateRequestDTO.getEmail() != null && !userUpdateRequestDTO.getEmail().isBlank() && !user.getEmail().equals(userUpdateRequestDTO.getEmail())) {
            if (userRepository.findByEmail(userUpdateRequestDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("사용중인 이메일입니다.");
            }
            user.setEmail(userUpdateRequestDTO.getEmail());
        }

        if (userUpdateRequestDTO.getNickName() != null && !userUpdateRequestDTO.getNickName().isBlank()) {
            user.setNickName(userUpdateRequestDTO.getNickName());
        }

        return new UserResponseDTO(user.getId(), user.getUserName(), user.getEmail(), user.getNickName());
    }
}
