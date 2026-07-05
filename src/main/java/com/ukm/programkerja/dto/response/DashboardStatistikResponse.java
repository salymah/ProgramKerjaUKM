package com.ukm.programkerja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response statistik untuk kartu-kartu di Dashboard (lihat mockup):
 * Total Program Kerja, Diajukan (MENUNGGU), Disetujui WK3, Selesai.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatistikResponse {

    private long totalProgramKerja;
    private long diajukan;
    private long disetujui;
    private long revisi;
    private long selesai;
}
