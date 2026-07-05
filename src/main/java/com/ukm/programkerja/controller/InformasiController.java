package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.InformasiRequest;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.InformasiResponse;
import com.ukm.programkerja.service.InformasiService;
import com.ukm.programkerja.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint Informasi/Agenda Kampus (mis. jadwal UAS, akreditasi), ditampilkan
 * di halaman Dashboard/Informasi pada frontend untuk seluruh role.
 */
@RestController
@RequestMapping("/api/informasi")
@RequiredArgsConstructor
@Tag(name = "Informasi", description = "Endpoint informasi & agenda kampus")
public class InformasiController {

    private final InformasiService informasiService;
    private final SecurityUtil securityUtil;

    @PostMapping
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<InformasiResponse>> create(@Valid @RequestBody InformasiRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(GlobalResponse.success("Informasi berhasil ditambahkan",
                informasiService.create(userId, request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<InformasiResponse>> update(@PathVariable Long id, @Valid @RequestBody InformasiRequest request) {
        return ResponseEntity.ok(GlobalResponse.success("Informasi berhasil diperbarui",
                informasiService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        informasiService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Informasi berhasil dihapus"));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<InformasiResponse>>> getAll() {
        return ResponseEntity.ok(GlobalResponse.success("Daftar informasi & agenda", informasiService.getAll()));
    }
}
