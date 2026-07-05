package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.LaporanResponse;
import com.ukm.programkerja.service.LaporanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Endpoint pengelolaan Laporan Kegiatan (UC-14 Lihat Laporan Kegiatan,
 * UC-15 Cetak Laporan).
 *
 * Aturan akses:
 * - Upload: role UKM
 * - Lihat & download: UKM (miliknya), WKIII, dan PEMBINA (semua, untuk evaluasi)
 * - Hapus: role WKIII
 */
@RestController
@RequestMapping("/api/laporan")
@RequiredArgsConstructor
@Tag(name = "Laporan", description = "Endpoint upload dan pengelolaan laporan kegiatan")
public class LaporanController {

    private final LaporanService laporanService;

    @PostMapping(value = "/program-kerja/{programKerjaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('UKM')")
    public ResponseEntity<GlobalResponse<LaporanResponse>> upload(
            @PathVariable Long programKerjaId,
            @RequestParam("file") MultipartFile file) {
        LaporanResponse response = laporanService.upload(programKerjaId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("Laporan berhasil diunggah", response));
    }

    @GetMapping("/program-kerja/{programKerjaId}")
    public ResponseEntity<GlobalResponse<List<LaporanResponse>>> getByProgramKerja(
            @PathVariable Long programKerjaId) {
        List<LaporanResponse> response = laporanService.getByProgramKerjaId(programKerjaId);
        return ResponseEntity.ok(GlobalResponse.success("Daftar laporan berhasil diambil", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WKIII', 'PEMBINA')")
    public ResponseEntity<GlobalResponse<List<LaporanResponse>>> getAll() {
        List<LaporanResponse> response = laporanService.getAll();
        return ResponseEntity.ok(GlobalResponse.success("Daftar seluruh laporan berhasil diambil", response));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = laporanService.download(id);
        String filename = laporanService.getFilenameById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        laporanService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Laporan berhasil dihapus"));
    }
}
