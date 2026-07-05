package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.response.ProposalResponse;
import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.Proposal;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.ProposalMapper;
import com.ukm.programkerja.repository.ProgramKerjaRepository;
import com.ukm.programkerja.repository.ProposalRepository;
import com.ukm.programkerja.service.ProposalService;
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
public class ProposalServiceImpl implements ProposalService {

    private static final String SUB_FOLDER = "proposal";

    private final ProposalRepository proposalRepository;
    private final ProgramKerjaRepository programKerjaRepository;
    private final ProposalMapper proposalMapper;
    private final FileStorageUtil fileStorageUtil;

    @Override
    public ProposalResponse upload(Long programKerjaId, MultipartFile file) {
        ProgramKerja programKerja = programKerjaRepository.findById(programKerjaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Program kerja dengan id " + programKerjaId + " tidak ditemukan"));

        String storedFilename = fileStorageUtil.store(file, SUB_FOLDER);

        Proposal proposal = Proposal.builder()
                .programKerja(programKerja)
                .namaFile(storedFilename)
                .tanggalUpload(LocalDate.now())
                .build();

        Proposal saved = proposalRepository.save(proposal);
        return proposalMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProposalResponse> getByProgramKerjaId(Long programKerjaId) {
        return proposalRepository.findByProgramKerjaId(programKerjaId)
                .stream()
                .map(proposalMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProposalResponse> getAll() {
        return proposalRepository.findAll()
                .stream()
                .map(proposalMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal dengan id " + proposalId + " tidak ditemukan"));

        try {
            var path = fileStorageUtil.getFilePath(proposal.getNamaFile(), SUB_FOLDER);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File proposal tidak ditemukan di server");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File proposal tidak ditemukan di server");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getFilenameById(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal dengan id " + proposalId + " tidak ditemukan"));
        return proposal.getNamaFile();
    }

    @Override
    public void delete(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposal dengan id " + proposalId + " tidak ditemukan"));

        fileStorageUtil.delete(proposal.getNamaFile(), SUB_FOLDER);
        proposalRepository.delete(proposal);
    }
}
