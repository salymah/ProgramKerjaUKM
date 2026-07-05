package com.ukm.programkerja.dto.response;

import com.ukm.programkerja.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response body data user. Tidak pernah menyertakan field password,
 * sesuai prinsip keamanan dasar.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String nama;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
    private Long ukmId;
    private String namaUkm;
}
