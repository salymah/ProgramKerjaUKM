package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.UserResponse;
import com.ukm.programkerja.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper manual (tanpa MapStruct) untuk konversi antara Entity User dan DTO.
 * Dipilih manual mapping agar dependency tetap minimal sesuai aturan
 * "jangan membuat struktur/dependency berlebihan".
 */
@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .nama(user.getNama())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .ukmId(user.getUkm() != null ? user.getUkm().getId() : null)
                .namaUkm(user.getUkm() != null ? user.getUkm().getNamaUkm() : null)
                .build();
    }
}
