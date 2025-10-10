package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.UserLoginRequestDTO;
import com.example.demo.domain.user.dto.UserResponseDTO;
import com.example.demo.domain.user.dto.UserSignUpRequestDTO;
import com.example.demo.domain.user.dto.UserUpdateRequestDTO;
import com.example.demo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Travel App User API!";
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserSignUpRequestDTO signUpRequestDTO) {
        UserResponseDTO responseDTO = userService.register(signUpRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
            @Valid @RequestBody UserLoginRequestDTO loginRequestDTO) {
        UserResponseDTO responseDTO = userService.login(loginRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> modify(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDTO updateRequestDTO) {
        UserResponseDTO responseDTO = userService.modify(userId, updateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
