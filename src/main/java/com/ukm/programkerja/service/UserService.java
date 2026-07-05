package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.UserCreateRequest;
import com.ukm.programkerja.dto.request.UserUpdateRequest;
import com.ukm.programkerja.dto.response.UserResponse;
import com.ukm.programkerja.entity.Role;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    List<UserResponse> getByRole(Role role);
}
