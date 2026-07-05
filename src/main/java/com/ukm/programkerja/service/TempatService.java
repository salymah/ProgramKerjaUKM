package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.TempatRequest;
import com.ukm.programkerja.dto.response.TempatResponse;

import java.util.List;

public interface TempatService {

    TempatResponse create(TempatRequest request);

    TempatResponse update(Long id, TempatRequest request);

    void delete(Long id);

    TempatResponse getById(Long id);

    List<TempatResponse> getAll();
}
