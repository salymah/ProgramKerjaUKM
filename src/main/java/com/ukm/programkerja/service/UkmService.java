package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.UkmRequest;
import com.ukm.programkerja.dto.response.UkmResponse;

import java.util.List;

public interface UkmService {
    UkmResponse create(UkmRequest request);
    UkmResponse update(Long id, UkmRequest request);
    void delete(Long id);
    UkmResponse getById(Long id);
    List<UkmResponse> getAll();
}
