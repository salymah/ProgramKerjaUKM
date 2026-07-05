package com.ukm.programkerja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempatResponse {

    private Long id;
    private String namaTempat;
    private String alamat;
    private String status;
}
