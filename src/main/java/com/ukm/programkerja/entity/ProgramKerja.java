package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity mapping untuk tabel `program_kerja`.
 *
 * Catatan audit:
 * - Tabel ini berfungsi ganda sebagai "program kerja" maupun "pengajuan"
 *   (tidak dibuat tabel `pengajuan` terpisah, sesuai keputusan penyatuan tabel).
 * - Status approval hanya 1 tahap (oleh WK III). Pembina tidak mengubah status,
 *   Pembina hanya membaca dan memberi Catatan (lihat Entity Catatan).
 */
@Entity
@Table(name = "program_kerja")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProgramKerja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * UKM/user yang mengajukan program kerja ini.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    /**
     * Tempat/lokasi kegiatan. Nullable karena UKM bisa mengajukan dulu,
     * lalu menentukan tempat belakangan (lihat use case "Daftar Tempat").
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempat_id")
    @ToString.Exclude
    private Tempat tempat;

    @Column(name = "nama_program", length = 150, nullable = false)
    private String namaProgram;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "tanggal_pengajuan")
    private LocalDate tanggalPengajuan;

    /**
     * Tanggal pelaksanaan kegiatan (berbeda dengan tanggal pengajuan).
     * Dipakai untuk mengecek bentrok jadwal pemakaian tempat antar
     * program kerja, lihat ProgramKerjaServiceImpl#cekBentrokTempat.
     */
    @Column(name = "tanggal_kegiatan")
    private LocalDate tanggalKegiatan;

    @Column(name = "tanggal_verifikasi")
    private LocalDate tanggalVerifikasi;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private StatusProgramKerja status = StatusProgramKerja.MENUNGGU;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @OneToMany(mappedBy = "programKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Proposal> proposalList = new ArrayList<>();

    @OneToMany(mappedBy = "programKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Laporan> laporanList = new ArrayList<>();

    @OneToMany(mappedBy = "programKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Catatan> catatanList = new ArrayList<>();
}
