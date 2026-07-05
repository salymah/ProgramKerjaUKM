package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity mapping untuk tabel `tempat`.
 * Menyimpan daftar lokasi yang dapat dipilih UKM saat mengajukan program kerja.
 */
@Entity
@Table(name = "tempat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Tempat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_tempat", length = 100, nullable = false)
    private String namaTempat;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    /**
     * Status ketersediaan tempat, contoh: "TERSEDIA", "TIDAK_TERSEDIA".
     * Disimpan sebagai varchar(50) di database (bukan enum native MySQL),
     * sehingga di Java diperlakukan sebagai String biasa agar tidak perlu
     * ALTER TABLE saat ada penambahan nilai baru di masa depan.
     */
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "tempat", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<ProgramKerja> programKerjaList = new ArrayList<>();
}
