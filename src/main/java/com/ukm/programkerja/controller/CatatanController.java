package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.CatatanRequest;
import com.ukm.programkerja.dto.response.CatatanResponse;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.service.CatatanService;
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
 * Endpoint pengelolaan Catatan/Saran (UC-09 Verifikasi Pengajuan bagian
 * catatan revisi, UC-16 Berikan Catatan/Saran).
 *
 * Aturan akses:
 * - Tambah catatan: WKIII dan PEMBINA (kolom wk3_id dipakai generik,
 *   lihat catatan audit di Entity Catatan)
 * - Lihat: semua role yang login (UKM perlu melihat catatan untuk
 *   menindaklanjuti revisi, UC-12)
 * - Hapus: WKIII
 */
@RestController
@RequestMapping("/api/catatan")
@RequiredArgsConstructor
@Tag(name = "Catatan", description = "Endpoint catatan/saran dari WK III dan Pembina")
public class CatatanController {

    private final CatatanService catatanService;
    private final SecurityUtil securityUtil;

    @PostMapping("/program-kerja/{programKerjaId}")
    @PreAuthorize("hasAnyRole('WKIII', 'PEMBINA')")
    public ResponseEntity<GlobalResponse<CatatanResponse>> create(
            @PathVariable Long programKerjaId,
            @Valid @RequestBody CatatanRequest request) {
        Long penulisId = securityUtil.getCurrentUserId();
        CatatanResponse response = catatanService.create(programKerjaId, penulisId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("Catatan berhasil ditambahkan", response));
    }

    @GetMapping("/program-kerja/{programKerjaId}")
    public ResponseEntity<GlobalResponse<List<CatatanResponse>>> getByProgramKerja(
            @PathVariable Long programKerjaId) {
        List<CatatanResponse> response = catatanService.getByProgramKerjaId(programKerjaId);
        return ResponseEntity.ok(GlobalResponse.success("Daftar catatan berhasil diambil", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        catatanService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Catatan berhasil dihapus"));
    }
}
