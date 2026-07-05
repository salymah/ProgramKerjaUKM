package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity mapping untuk tabel `informasi`.
 * Menyimpan agenda/pengumuman kampus (mis. jadwal UAS, akreditasi) yang
 * ditampilkan di halaman Dashboard/Informasi pada frontend, dapat dikelola
 * oleh WK III dan dilihat oleh seluruh role.
 */
@Entity
@Table(name = "informasi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Informasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "judul", length = 200, nullable = false)
    private String judul;

    @Column(name = "isi", columnDefinition = "TEXT")
    private String isi;

    @Enumerated(EnumType.STRING)
    @Column(name = "kategori", nullable = false)
    @Builder.Default
    private KategoriInformasi kategori = KategoriInformasi.PENGUMUMAN;

    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dibuat_oleh")
    @ToString.Exclude
    private User dibuatOleh;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
