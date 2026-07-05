package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.ProposalResponse;
import com.ukm.programkerja.entity.Proposal;
import org.springframework.stereotype.Component;

@Component
public class ProposalMapper {

    public ProposalResponse toResponse(Proposal proposal) {
        if (proposal == null) {
            return null;
        }
        return ProposalResponse.builder()
                .id(proposal.getId())
                .programKerjaId(proposal.getProgramKerja() != null ? proposal.getProgramKerja().getId() : null)
                .namaProgram(proposal.getProgramKerja() != null ? proposal.getProgramKerja().getNamaProgram() : null)
                .namaFile(proposal.getNamaFile())
                .tanggalUpload(proposal.getTanggalUpload())
                .build();
    }
}
