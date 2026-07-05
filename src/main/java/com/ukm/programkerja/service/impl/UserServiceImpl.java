package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.UserCreateRequest;
import com.ukm.programkerja.dto.request.UserUpdateRequest;
import com.ukm.programkerja.dto.response.UserResponse;
import com.ukm.programkerja.entity.Role;
import com.ukm.programkerja.entity.Ukm;
import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.exception.DuplicateResourceException;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.UserMapper;
import com.ukm.programkerja.repository.UkmRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UkmRepository ukmRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' sudah digunakan");
        }

        Ukm ukm = null;
        if (request.getUkmId() != null) {
            ukm = ukmRepository.findById(request.getUkmId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "UKM dengan id " + request.getUkmId() + " tidak ditemukan"));
        }

        User user = User.builder()
                .nama(request.getNama())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .ukm(ukm)
                .build();

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + id + " tidak ditemukan"));

        // Jika username diubah, pastikan username baru belum dipakai user lain
        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' sudah digunakan");
        }

        user.setNama(request.getNama());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        if (request.getUkmId() != null) {
            Ukm ukm = ukmRepository.findById(request.getUkmId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "UKM dengan id " + request.getUkmId() + " tidak ditemukan"));
            user.setUkm(ukm);
        } else {
            user.setUkm(null);
        }

        // Password opsional: hanya diubah jika dikirim dan tidak kosong
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + id + " tidak ditemukan"));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + id + " tidak ditemukan"));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
