package com.ukm.programkerja.entity;

/**
 * Status pengajuan/program kerja.
 * MENUNGGU  -> baru diajukan, belum diverifikasi WK III
 * REVISI    -> WK III meminta perbaikan/revisi
 * DISETUJUI -> disetujui oleh WK III, kegiatan boleh dilaksanakan
 * SELESAI   -> kegiatan telah dilaksanakan & laporan diunggah
 */
public enum StatusProgramKerja {
    MENUNGGU,
    REVISI,
    DISETUJUI,
    SELESAI
}
