package com.ukm.programkerja.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity mapping untuk tabel `reset_password_request`.
 * Karena sistem ini tidak memiliki infrastruktur email, fitur "Lupa
 * Password" diimplementasikan sebagai alur permintaan: pengguna mengajukan
 * permintaan lewat halaman login (tanpa perlu login), lalu WK III meninjau
 * daftar permintaan dan mengatur ulang password pengguna terkait secara
 * manual lewat halaman Kelola Pengguna.
 */
@Entity
@Table(name = "reset_password_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResetPasswordRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "alasan", columnDefinition = "TEXT")
    private String alasan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ResetStatus status = ResetStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "diproses_at")
    private LocalDateTime diprosesAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
