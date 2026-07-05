package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.NotifikasiItemResponse;
import com.ukm.programkerja.service.NotifikasiService;
import com.ukm.programkerja.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifikasi")
@RequiredArgsConstructor
@Tag(name = "Notifikasi", description = "Notifikasi dihitung on-the-fly sesuai role pengguna")
public class NotifikasiController {

    private final NotifikasiService notifikasiService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<NotifikasiItemResponse>>> getNotifikasi() {
        Long userId = securityUtil.getCurrentUserId();
        return ResponseEntity.ok(GlobalResponse.success("Daftar notifikasi", notifikasiService.getNotifikasi(userId)));
    }
}
