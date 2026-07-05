package com.ukm.programkerja.dto.request;

import com.ukm.programkerja.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body untuk memperbarui data user.
 * Password bersifat opsional: jika dikirim kosong/null, password lama tidak diubah.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 100, message = "Nama maksimal 100 karakter")
    private String nama;

    @NotBlank(message = "Username wajib diisi")
    @Size(max = 50, message = "Username maksimal 50 karakter")
    private String username;

    /**
     * Opsional. Jika null/kosong, password lama dipertahankan.
     */
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    @NotNull(message = "Role wajib diisi")
    private Role role;

    /** Opsional, hanya relevan jika role = UKM. */
    private Long ukmId;
}
