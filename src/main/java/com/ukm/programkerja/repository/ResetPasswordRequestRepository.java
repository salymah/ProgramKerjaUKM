package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.ResetPasswordRequest;
import com.ukm.programkerja.entity.ResetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, Long> {
    List<ResetPasswordRequest> findByStatusOrderByCreatedAtDesc(ResetStatus status);
    List<ResetPasswordRequest> findAllByOrderByCreatedAtDesc();
}
