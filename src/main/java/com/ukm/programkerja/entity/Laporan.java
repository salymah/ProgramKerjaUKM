package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Entity mapping untuk tabel `laporan`.
 * Menyimpan metadata file laporan kegiatan yang diunggah UKM setelah
 * program kerja terlaksana.
 */
@Entity
@Table(name = "laporan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Laporan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    @ToString.Exclude
    private ProgramKerja programKerja;

    @Column(name = "nama_file", length = 255)
    private String namaFile;

    @Column(name = "tanggal_upload")
    private LocalDate tanggalUpload;

    /**
     * Status laporan, contoh: "DITERIMA", "PERLU_REVISI".
     * Disimpan sebagai varchar(50), diperlakukan sebagai String biasa.
     */
    @Column(name = "status", length = 50)
    private String status;
}
