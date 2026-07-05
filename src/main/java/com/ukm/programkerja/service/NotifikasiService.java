package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.response.NotifikasiItemResponse;

import java.util.List;

public interface NotifikasiService {
    /** Menghitung daftar notifikasi on-the-fly untuk user yang sedang login. */
    List<NotifikasiItemResponse> getNotifikasi(Long userId);
}
