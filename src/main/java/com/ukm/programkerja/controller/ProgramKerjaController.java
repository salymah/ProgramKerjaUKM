package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.ProgramKerjaCreateRequest;
import com.ukm.programkerja.dto.request.ProgramKerjaUpdateRequest;
import com.ukm.programkerja.dto.request.VerifikasiRequest;
import com.ukm.programkerja.dto.response.DashboardStatistikResponse;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.ProgramKerjaResponse;
import com.ukm.programkerja.service.ProgramKerjaService;
import com.ukm.programkerja.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint pengelolaan Program Kerja.
 *
 * Aturan akses:
 * - Ajukan/lihat-sendiri/ubah-sendiri: role UKM
 * - Verifikasi (setuju/revisi), lihat semua: role WKIII
 * - Statistik dashboard: semua role yang login (ditampilkan beda konteks
 *   di frontend tergantung role)
 */
@RestController
@RequestMapping("/api/program-kerja")
@RequiredArgsConstructor
@Tag(name = "Program Kerja", description = "Endpoint pengelolaan program kerja UKM")
public class ProgramKerjaController {

    private final ProgramKerjaService programKerjaService;
    private final SecurityUtil securityUtil;

    @PostMapping
    @PreAuthorize("hasRole('UKM')")
    public ResponseEntity<GlobalResponse<ProgramKerjaResponse>> create(
            @Valid @RequestBody ProgramKerjaCreateRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        ProgramKerjaResponse response = programKerjaService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("Program kerja berhasil diajukan", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UKM')")
    public ResponseEntity<GlobalResponse<ProgramKerjaResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProgramKerjaUpdateRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        ProgramKerjaResponse response = programKerjaService.update(id, userId, request);
        return ResponseEntity.ok(GlobalResponse.success("Program kerja berhasil diperbarui", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        programKerjaService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Program kerja berhasil dihapus"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<ProgramKerjaResponse>> getById(@PathVariable Long id) {
        ProgramKerjaResponse response = programKerjaService.getById(id);
        return ResponseEntity.ok(GlobalResponse.success("Data program kerja ditemukan", response));
    }

    /**
     * Daftar program kerja. WKIII melihat semua data, UKM hanya melihat
     * miliknya sendiri (otomatis difilter berdasarkan token, tidak perlu
     * parameter tambahan dari client).
     */
    @GetMapping
    public ResponseEntity<GlobalResponse<List<ProgramKerjaResponse>>> getAll() {
        var currentUser = securityUtil.getCurrentUser();
        List<ProgramKerjaResponse> response;

        if (currentUser.getRole().name().equals("UKM")) {
            response = programKerjaService.getByUserId(currentUser.getId());
        } else {
            response = programKerjaService.getAll();
        }

        return ResponseEntity.ok(GlobalResponse.success("Daftar program kerja berhasil diambil", response));
    }

    @PatchMapping("/{id}/verifikasi")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<ProgramKerjaResponse>> verifikasi(
            @PathVariable Long id,
            @Valid @RequestBody VerifikasiRequest request) {
        ProgramKerjaResponse response = programKerjaService.verifikasi(id, request);
        return ResponseEntity.ok(GlobalResponse.success("Verifikasi program kerja berhasil disimpan", response));
    }

    @GetMapping("/statistik")
    public ResponseEntity<GlobalResponse<DashboardStatistikResponse>> getStatistik() {
        DashboardStatistikResponse response = programKerjaService.getStatistik();
        return ResponseEntity.ok(GlobalResponse.success("Statistik dashboard berhasil diambil", response));
    }
}
