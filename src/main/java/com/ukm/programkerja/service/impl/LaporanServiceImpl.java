package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.response.LaporanResponse;
import com.ukm.programkerja.entity.Laporan;
import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.StatusProgramKerja;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.LaporanMapper;
import com.ukm.programkerja.repository.LaporanRepository;
import com.ukm.programkerja.repository.ProgramKerjaRepository;
import com.ukm.programkerja.service.LaporanService;
import com.ukm.programkerja.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LaporanServiceImpl implements LaporanService {

    private static final String SUB_FOLDER = "laporan";

    private final LaporanRepository laporanRepository;
    private final ProgramKerjaRepository programKerjaRepository;
    private final LaporanMapper laporanMapper;
    private final FileStorageUtil fileStorageUtil;

    @Override
    public LaporanResponse upload(Long programKerjaId, MultipartFile file) {
        ProgramKerja programKerja = programKerjaRepository.findById(programKerjaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Program kerja dengan id " + programKerjaId + " tidak ditemukan"));

        String storedFilename = fileStorageUtil.store(file, SUB_FOLDER);

        Laporan laporan = Laporan.builder()
                .programKerja(programKerja)
                .namaFile(storedFilename)
                .tanggalUpload(LocalDate.now())
                .status("DITERIMA")
                .build();

        Laporan saved = laporanRepository.save(laporan);

        // Audit note: laporan hanya diunggah setelah kegiatan terlaksana
        // (lihat Activity Diagram 3 Aktor: Upload Kegiatan setelah
        // Melaksanakan Kegiatan), sehingga status program kerja otomatis
        // berubah menjadi SELESAI saat laporan pertama diunggah.
        if (programKerja.getStatus() != StatusProgramKerja.SELESAI) {
            programKerja.setStatus(StatusProgramKerja.SELESAI);
            programKerjaRepository.save(programKerja);
        }

        return laporanMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaporanResponse> getByProgramKerjaId(Long programKerjaId) {
        return laporanRepository.findByProgramKerjaId(programKerjaId)
                .stream()
                .map(laporanMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaporanResponse> getAll() {
        return laporanRepository.findAll()
                .stream()
                .map(laporanMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long laporanId) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laporan dengan id " + laporanId + " tidak ditemukan"));

        try {
            var path = fileStorageUtil.getFilePath(laporan.getNamaFile(), SUB_FOLDER);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File laporan tidak ditemukan di server");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File laporan tidak ditemukan di server");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getFilenameById(Long laporanId) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laporan dengan id " + laporanId + " tidak ditemukan"));
        return laporan.getNamaFile();
    }

    @Override
    public void delete(Long laporanId) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Laporan dengan id " + laporanId + " tidak ditemukan"));

        fileStorageUtil.delete(laporan.getNamaFile(), SUB_FOLDER);
        laporanRepository.delete(laporan);
    }
}
