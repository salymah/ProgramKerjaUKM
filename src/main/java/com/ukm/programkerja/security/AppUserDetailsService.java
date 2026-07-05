package com.ukm.programkerja.security;

import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementasi UserDetailsService yang dipakai Spring Security untuk
 * memuat data user berdasarkan username saat proses autentikasi.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User dengan username '" + username + "' tidak ditemukan"));
        return new AppUserDetails(user);
    }
}
