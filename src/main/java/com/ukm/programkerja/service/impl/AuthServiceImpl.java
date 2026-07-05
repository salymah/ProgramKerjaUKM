package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.LoginRequest;
import com.ukm.programkerja.dto.request.LupaPasswordRequest;
import com.ukm.programkerja.dto.request.ProsesResetPasswordRequest;
import com.ukm.programkerja.dto.response.LoginResponse;
import com.ukm.programkerja.dto.response.ResetPasswordRequestResponse;
import com.ukm.programkerja.entity.ResetPasswordRequest;
import com.ukm.programkerja.entity.ResetStatus;
import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.exception.InvalidCredentialsException;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.UserMapper;
import com.ukm.programkerja.repository.ResetPasswordRequestRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.security.JwtUtil;
import com.ukm.programkerja.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Username atau password salah");
        }

        String token = jwtUtil.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Username atau password salah"));

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public void ajukanLupaPassword(LupaPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Username '" + request.getUsername() + "' tidak ditemukan"));

        ResetPasswordRequest resetRequest = ResetPasswordRequest.builder()
                .user(user)
                .alasan(request.getAlasan())
                .status(ResetStatus.PENDING)
                .build();
        resetPasswordRequestRepository.save(resetRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResetPasswordRequestResponse> getDaftarPermintaanReset() {
        return resetPasswordRequestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(r -> ResetPasswordRequestResponse.builder()
                        .id(r.getId())
                        .userId(r.getUser().getId())
                        .namaUser(r.getUser().getNama())
                        .username(r.getUser().getUsername())
                        .alasan(r.getAlasan())
                        .status(r.getStatus())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public void prosesResetPassword(Long requestId, ProsesResetPasswordRequest request) {
        ResetPasswordRequest resetRequest = resetPasswordRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permintaan reset password dengan id " + requestId + " tidak ditemukan"));

        User user = resetRequest.getUser();
        user.setPassword(passwordEncoder.encode(request.getPasswordBaru()));
        userRepository.save(user);

        resetRequest.setStatus(ResetStatus.SELESAI);
        resetRequest.setDiprosesAt(LocalDateTime.now());
        resetPasswordRequestRepository.save(resetRequest);
    }
}
