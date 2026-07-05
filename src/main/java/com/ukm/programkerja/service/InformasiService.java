package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.InformasiRequest;
import com.ukm.programkerja.dto.response.InformasiResponse;

import java.util.List;

public interface InformasiService {
    InformasiResponse create(Long userId, InformasiRequest request);
    InformasiResponse update(Long id, InformasiRequest request);
    void delete(Long id);
    List<InformasiResponse> getAll();
}
