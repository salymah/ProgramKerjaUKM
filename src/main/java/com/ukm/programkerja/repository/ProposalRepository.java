package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByProgramKerjaId(Long programKerjaId);
}
