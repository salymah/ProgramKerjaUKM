package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.response.NotifikasiItemResponse;
import com.ukm.programkerja.entity.*;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.repository.CatatanRepository;
import com.ukm.programkerja.repository.ProgramKerjaRepository;
import com.ukm.programkerja.repository.ResetPasswordRequestRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.service.NotifikasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifikasi dihitung ON-THE-FLY dari kondisi data ProgramKerja/ResetPasswordRequest
 * terkini setiap kali endpoint dipanggil — TIDAK disimpan permanen di tabel
 * tersendiri. Ini konsisten dengan keputusan desain yang disepakati di awal
 * pembangunan sistem (lihat dokumen audit database): riwayat baca/belum baca
 * sengaja tidak diimplementasikan demi menjaga skema database tetap sederhana.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotifikasiServiceImpl implements NotifikasiService {

    private final ProgramKerjaRepository programKerjaRepository;
    private final CatatanRepository catatanRepository;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotifikasiItemResponse> getNotifikasi(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + userId + " tidak ditemukan"));

        List<NotifikasiItemResponse> result = new ArrayList<>();

        switch (user.getRole()) {
            case UKM -> {
                // UKM: notifikasi untuk pengajuan miliknya yang perlu revisi atau baru disetujui
                List<ProgramKerja> milik = programKerjaRepository.findByUserId(userId);
                for (ProgramKerja pk : milik) {
                    if (pk.getStatus() == StatusProgramKerja.REVISI) {
                        result.add(NotifikasiItemResponse.builder()
                                .tipe("PERLU_REVISI")
                                .judul("Perlu Revisi")
                                .pesan("Program kerja \"" + pk.getNamaProgram() + "\" perlu direvisi. Periksa catatan dari WK III.")
                                .programKerjaId(pk.getId())
                                .tingkat("WARNING")
                                .build());
                    } else if (pk.getStatus() == StatusProgramKerja.DISETUJUI) {
                        result.add(NotifikasiItemResponse.builder()
                                .tipe("DISETUJUI")
                                .judul("Program Kerja Disetujui")
                                .pesan("Program kerja \"" + pk.getNamaProgram() + "\" telah disetujui WK III. Silakan unggah laporan setelah kegiatan selesai.")
                                .programKerjaId(pk.getId())
                                .tingkat("INFO")
                                .build());
                    }
                }
            }
            case WKIII -> {
                // WKIII: notifikasi pengajuan yang menunggu verifikasi + permintaan reset password
                List<ProgramKerja> menunggu = programKerjaRepository.findByStatus(StatusProgramKerja.MENUNGGU);
                for (ProgramKerja pk : menunggu) {
                    result.add(NotifikasiItemResponse.builder()
                            .tipe("PERLU_VERIFIKASI")
                            .judul("Menunggu Verifikasi")
                            .pesan("Program kerja \"" + pk.getNamaProgram() + "\" dari " +
                                    (pk.getUser() != null ? pk.getUser().getNama() : "-") + " menunggu verifikasi.")
                            .programKerjaId(pk.getId())
                            .tingkat("WARNING")
                            .build());
                }
                long resetPending = resetPasswordRequestRepository
                        .findByStatusOrderByCreatedAtDesc(ResetStatus.PENDING).size();
                if (resetPending > 0) {
                    result.add(NotifikasiItemResponse.builder()
                            .tipe("RESET_PASSWORD")
                            .judul("Permintaan Reset Password")
                            .pesan("Ada " + resetPending + " permintaan reset password yang belum diproses.")
                            .tingkat("DANGER")
                            .build());
                }
            }
            case PEMBINA -> {
                // PEMBINA: program kerja SELESAI yang belum pernah diberi catatan
                List<ProgramKerja> selesai = programKerjaRepository.findByStatus(StatusProgramKerja.SELESAI);
                for (ProgramKerja pk : selesai) {
                    boolean sudahAdaCatatan = !catatanRepository.findByProgramKerjaId(pk.getId()).isEmpty();
                    if (!sudahAdaCatatan) {
                        result.add(NotifikasiItemResponse.builder()
                                .tipe("PERLU_CATATAN")
                                .judul("Perlu Evaluasi")
                                .pesan("Program kerja \"" + pk.getNamaProgram() + "\" sudah selesai dan belum diberi catatan/saran.")
                                .programKerjaId(pk.getId())
                                .tingkat("INFO")
                                .build());
                    }
                }
            }
        }
        return result;
    }
}
