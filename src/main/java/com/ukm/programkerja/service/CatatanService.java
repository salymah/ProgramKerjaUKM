package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.CatatanRequest;
import com.ukm.programkerja.dto.response.CatatanResponse;

import java.util.List;

public interface CatatanService {

    /**
     * Menambahkan catatan baru. penulisId diambil dari user yang sedang
     * login (WKIII atau PEMBINA), bukan dari request body.
     */
    CatatanResponse create(Long programKerjaId, Long penulisId, CatatanRequest request);

    List<CatatanResponse> getByProgramKerjaId(Long programKerjaId);

    void delete(Long id);
}
