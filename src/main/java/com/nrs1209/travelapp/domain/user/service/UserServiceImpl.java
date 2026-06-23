package com.nrs1209.travelapp.domain.user.service;

import com.nrs1209.travelapp.domain.user.dto.*;
import com.nrs1209.travelapp.domain.user.entity.User;
import com.nrs1209.travelapp.domain.user.repository.UserRepository;
import com.nrs1209.travelapp.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userRepository.findByUserName(userLoginRequestDTO.getUserName())
            .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUserName());

        return UserResponseDTO.builder()
                .userid(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .token(token)
                .build();
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

        return UserResponseDTO.builder()
                .userid(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .nickName(savedUser.getNickName())
                .build();
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

        return UserResponseDTO.builder()
                .userid(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        // DB에서 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        // DTO로 변환해 리턴
        return UserResponseDTO.builder()
                .userid(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public FindUsernameResponseDTO findUsername(FindUsernameRequestDTO requestDTO) {
        User user = userRepository.findByNickNameAndEmail(requestDTO.getNickName(), requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("입력하신 정보와 일치하는 회원이 없습니다."));

        return new FindUsernameResponseDTO(user.getUserName());
    }

    @Override
    @Transactional
    public ResetPasswordResponseDTO resetPassword(ResetPasswordRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 회원이 없습니다."));

        // 8자리의 랜덤 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // 중요: 로그인 검증 시 passwordEncoder를 사용하니, 임시 비밀번호도 암호화 해 저장
        String encodedPassword = passwordEncoder.encode(tempPassword);
        user.setPassword(encodedPassword); // 더티 체킹에 의해 자동 반영

        return new ResetPasswordResponseDTO("임시 비밀번호가 발급되었습니다.", tempPassword);
    }
}
