package com.nrs1209.travelapp.domain.user.controller;

import com.nrs1209.travelapp.domain.user.dto.UserLoginRequestDTO;
import com.nrs1209.travelapp.domain.user.dto.UserResponseDTO;
import com.nrs1209.travelapp.domain.user.dto.UserSignUpRequestDTO;
import com.nrs1209.travelapp.domain.user.dto.UserUpdateRequestDTO;
import com.nrs1209.travelapp.domain.user.service.UserService;
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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@RequestParam Long userId) {
        UserResponseDTO responseDTO = userService.getUserById(userId);
        return ResponseEntity.ok(responseDTO);
    }
}
