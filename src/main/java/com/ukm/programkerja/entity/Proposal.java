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
 * Entity mapping untuk tabel `proposal`.
 * Menyimpan metadata file proposal yang diunggah UKM untuk suatu program kerja.
 */
@Entity
@Table(name = "proposal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Proposal {

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
}
