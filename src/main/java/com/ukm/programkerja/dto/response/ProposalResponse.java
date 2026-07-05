package com.ukm.programkerja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponse {

    private Long id;
    private Long programKerjaId;
    private String namaProgram;
    private String namaFile;
    private LocalDate tanggalUpload;
}
