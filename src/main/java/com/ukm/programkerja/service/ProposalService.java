package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.response.ProposalResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProposalService {

    ProposalResponse upload(Long programKerjaId, MultipartFile file);

    List<ProposalResponse> getByProgramKerjaId(Long programKerjaId);

    List<ProposalResponse> getAll();

    Resource download(Long proposalId);

    String getFilenameById(Long proposalId);

    void delete(Long proposalId);
}
