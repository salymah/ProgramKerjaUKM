package com.ukm.programkerja.util;

import com.ukm.programkerja.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility untuk mengambil informasi user yang sedang login (dari token JWT
 * yang sudah divalidasi JwtAuthenticationFilter), dipakai di Controller
 * agar operasi seperti "ajukan program kerja" selalu terikat ke identitas
 * pengguna yang sebenarnya, bukan ID yang dikirim manual oleh client.
 */
@Component
public class SecurityUtil {

    public AppUserDetails getCurrentUser() {
        return (AppUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
