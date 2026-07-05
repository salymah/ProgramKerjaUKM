package com.ukm.programkerja.dto.response;

import com.ukm.programkerja.entity.ResetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequestResponse {
    private Long id;
    private Long userId;
    private String namaUser;
    private String username;
    private String alasan;
    private ResetStatus status;
    private LocalDateTime createdAt;
}
