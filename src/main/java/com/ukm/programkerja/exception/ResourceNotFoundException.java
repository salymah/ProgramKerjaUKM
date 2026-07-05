package com.ukm.programkerja.exception;

/**
 * Dilempar ketika resource (User, ProgramKerja, dll) tidak ditemukan di database.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
