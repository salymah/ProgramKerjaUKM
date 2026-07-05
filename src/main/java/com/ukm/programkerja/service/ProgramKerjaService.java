package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.ProgramKerjaCreateRequest;
import com.ukm.programkerja.dto.request.ProgramKerjaUpdateRequest;
import com.ukm.programkerja.dto.request.VerifikasiRequest;
import com.ukm.programkerja.dto.response.DashboardStatistikResponse;
import com.ukm.programkerja.dto.response.ProgramKerjaResponse;

import java.util.List;

public interface ProgramKerjaService {

    /**
     * Mengajukan program kerja baru. userId diambil dari user yang sedang
     * login (UKM), bukan dari request body, demi keamanan (mencegah UKM
     * mengajukan program kerja atas nama UKM lain).
     */
    ProgramKerjaResponse create(Long userId, ProgramKerjaCreateRequest request);

    ProgramKerjaResponse update(Long id, Long requesterId, ProgramKerjaUpdateRequest request);

    void delete(Long id);

    ProgramKerjaResponse getById(Long id);

    List<ProgramKerjaResponse> getAll();

    List<ProgramKerjaResponse> getByUserId(Long userId);

    /**
     * Verifikasi pengajuan oleh WK III (setuju atau minta revisi).
     * Mengisi tanggalVerifikasi secara otomatis.
     */
    ProgramKerjaResponse verifikasi(Long id, VerifikasiRequest request);

    DashboardStatistikResponse getStatistik();
}
