package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.TempatResponse;
import com.ukm.programkerja.entity.Tempat;
import org.springframework.stereotype.Component;

@Component
public class TempatMapper {

    public TempatResponse toResponse(Tempat tempat) {
        if (tempat == null) {
            return null;
        }
        return TempatResponse.builder()
                .id(tempat.getId())
                .namaTempat(tempat.getNamaTempat())
                .alamat(tempat.getAlamat())
                .status(tempat.getStatus())
                .build();
    }
}
