package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.TempatRequest;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.TempatResponse;
import com.ukm.programkerja.service.TempatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint pengelolaan data Tempat kegiatan.
 *
 * Aturan akses:
 * - Melihat daftar/detail tempat: semua role yang sudah login (UKM perlu
 *   ini untuk memilih lokasi saat mengajukan program kerja, lihat UC-04).
 * - Tambah/ubah/hapus tempat: hanya WKIII.
 */
@RestController
@RequestMapping("/api/tempat")
@RequiredArgsConstructor
@Tag(name = "Tempat", description = "Endpoint pengelolaan data tempat kegiatan")
public class TempatController {

    private final TempatService tempatService;

    @PostMapping
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<TempatResponse>> create(@Valid @RequestBody TempatRequest request) {
        TempatResponse response = tempatService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("Tempat berhasil ditambahkan", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<TempatResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TempatRequest request) {
        TempatResponse response = tempatService.update(id, request);
        return ResponseEntity.ok(GlobalResponse.success("Tempat berhasil diperbarui", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        tempatService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Tempat berhasil dihapus"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<TempatResponse>> getById(@PathVariable Long id) {
        TempatResponse response = tempatService.getById(id);
        return ResponseEntity.ok(GlobalResponse.success("Data tempat ditemukan", response));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TempatResponse>>> getAll() {
        List<TempatResponse> response = tempatService.getAll();
        return ResponseEntity.ok(GlobalResponse.success("Daftar tempat berhasil diambil", response));
    }
}
