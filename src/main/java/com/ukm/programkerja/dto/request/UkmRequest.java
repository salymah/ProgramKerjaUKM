package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UkmRequest {

    @NotBlank(message = "Nama UKM wajib diisi")
    private String namaUkm;

    @NotBlank(message = "Bidang wajib diisi")
    private String bidang;

    private String deskripsi;

    private String status;
}
