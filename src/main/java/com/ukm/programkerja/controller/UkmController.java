package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.UkmRequest;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.UkmResponse;
import com.ukm.programkerja.service.UkmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint UC-08 "Kelola Data UKM". Lihat (GET) dapat diakses semua role
 * yang login (dipakai juga untuk dropdown pemilihan UKM saat membuat akun
 * baru), kelola (POST/PUT/DELETE) khusus WK III.
 */
@RestController
@RequestMapping("/api/ukm")
@RequiredArgsConstructor
@Tag(name = "UKM", description = "Endpoint pengelolaan data organisasi UKM (UC-08)")
public class UkmController {

    private final UkmService ukmService;

    @PostMapping
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<UkmResponse>> create(@Valid @RequestBody UkmRequest request) {
        return ResponseEntity.ok(GlobalResponse.success("UKM berhasil ditambahkan", ukmService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<UkmResponse>> update(@PathVariable Long id, @Valid @RequestBody UkmRequest request) {
        return ResponseEntity.ok(GlobalResponse.success("UKM berhasil diperbarui", ukmService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        ukmService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("UKM berhasil dihapus"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<UkmResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(GlobalResponse.success("Detail UKM", ukmService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<UkmResponse>>> getAll() {
        return ResponseEntity.ok(GlobalResponse.success("Daftar UKM", ukmService.getAll()));
    }
}
