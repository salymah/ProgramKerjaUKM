package com.ukm.programkerja.exception;

/**
 * Dilempar ketika ada percobaan menyimpan data yang melanggar constraint
 * keunikan (contoh: username sudah terdaftar).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
