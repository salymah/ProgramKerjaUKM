package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity mapping untuk tabel `ukm` (organisasi mahasiswa).
 * Ditambahkan untuk mengimplementasikan UC-08 "Kelola Data UKM" yang
 * sebelumnya disederhanakan menjadi CRUD User biasa. Sekarang UKM punya
 * data tersendiri (nama, bidang, deskripsi), dan akun login ber-role UKM
 * dapat dikaitkan ke salah satu baris di tabel ini lewat kolom users.ukm_id.
 */
@Entity
@Table(name = "ukm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Ukm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_ukm", length = 100, nullable = false)
    private String namaUkm;

    @Column(name = "bidang", length = 100, nullable = false)
    private String bidang;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "AKTIF";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "ukm", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<User> anggotaList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
