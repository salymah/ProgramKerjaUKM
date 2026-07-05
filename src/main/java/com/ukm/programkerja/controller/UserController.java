package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.UserCreateRequest;
import com.ukm.programkerja.dto.request.UserUpdateRequest;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.UserResponse;
import com.ukm.programkerja.entity.Role;
import com.ukm.programkerja.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint pengelolaan data User. Dibatasi hanya untuk role WKIII,
 * karena pengelolaan akun pengguna (termasuk akun UKM & Pembina) adalah
 * tanggung jawab administratif WK III pada sistem ini.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoint pengelolaan data pengguna")
@PreAuthorize("hasRole('WKIII')")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<GlobalResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("User berhasil dibuat", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(GlobalResponse.success("User berhasil diperbarui", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("User berhasil dihapus"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(GlobalResponse.success("Data user ditemukan", response));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<UserResponse>>> getAll(
            @RequestParam(required = false) Role role) {
        List<UserResponse> response = (role != null) ? userService.getByRole(role) : userService.getAll();
        return ResponseEntity.ok(GlobalResponse.success("Daftar user berhasil diambil", response));
    }
}
