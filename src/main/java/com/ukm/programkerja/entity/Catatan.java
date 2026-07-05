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
 * Entity mapping untuk tabel `catatan`.
 *
 * Catatan audit PENTING:
 * Kolom asli di database bernama `wk3_id`, namun secara fungsional kolom ini
 * dipakai generik untuk menyimpan ID siapa pun yang menulis catatan/saran —
 * baik WK III (saat verifikasi pengajuan) maupun Pembina (saat memberi
 * catatan/saran atas laporan kegiatan, lihat UC-16).
 *
 * Keputusan: TIDAK mengubah nama kolom di database (menghindari ALTER TABLE
 * yang tidak perlu), field Java diberi nama generik `penulis` agar kode lebih
 * jelas dibaca dan tidak menyiratkan hanya milik WK III.
 */
@Entity
@Table(name = "catatan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Catatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    @ToString.Exclude
    private ProgramKerja programKerja;

    /**
     * Penulis catatan. Mapping ke kolom database `wk3_id`, tapi nilainya
     * bisa berupa ID user dengan role WKIII ATAU PEMBINA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wk3_id")
    @ToString.Exclude
    private User penulis;

    @Column(name = "isi_catatan", columnDefinition = "TEXT")
    private String isiCatatan;

    @Column(name = "tanggal")
    private LocalDate tanggal;
}
