package com.ukm.programkerja.exception;

/**
 * Dilempar ketika username atau password yang dimasukkan saat login salah.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
