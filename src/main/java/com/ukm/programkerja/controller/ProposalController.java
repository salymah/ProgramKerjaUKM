package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.ProposalResponse;
import com.ukm.programkerja.service.ProposalService;
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
 * Endpoint pengelolaan Proposal (UC-03 Upload Proposal, UC-06 Lihat Proposal).
 *
 * Aturan akses:
 * - Upload: role UKM
 * - Lihat & download: UKM (miliknya) dan WKIII (semua, untuk verifikasi)
 * - Hapus: role WKIII (administratif)
 */
@RestController
@RequestMapping("/api/proposal")
@RequiredArgsConstructor
@Tag(name = "Proposal", description = "Endpoint upload dan pengelolaan dokumen proposal")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping(value = "/program-kerja/{programKerjaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('UKM')")
    public ResponseEntity<GlobalResponse<ProposalResponse>> upload(
            @PathVariable Long programKerjaId,
            @RequestParam("file") MultipartFile file) {
        ProposalResponse response = proposalService.upload(programKerjaId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success("Proposal berhasil diunggah", response));
    }

    @GetMapping("/program-kerja/{programKerjaId}")
    public ResponseEntity<GlobalResponse<List<ProposalResponse>>> getByProgramKerja(
            @PathVariable Long programKerjaId) {
        List<ProposalResponse> response = proposalService.getByProgramKerjaId(programKerjaId);
        return ResponseEntity.ok(GlobalResponse.success("Daftar proposal berhasil diambil", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<List<ProposalResponse>>> getAll() {
        List<ProposalResponse> response = proposalService.getAll();
        return ResponseEntity.ok(GlobalResponse.success("Daftar seluruh proposal berhasil diambil", response));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = proposalService.download(id);
        String filename = proposalService.getFilenameById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> delete(@PathVariable Long id) {
        proposalService.delete(id);
        return ResponseEntity.ok(GlobalResponse.success("Proposal berhasil dihapus"));
    }
}
