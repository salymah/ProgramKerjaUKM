package com.ukm.programkerja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point aplikasi Sistem Informasi Manajemen Program Kerja UKM.
 *
 * Backend berjalan pada http://localhost:8082
 * Frontend (Vue.js 3) berjalan pada http://localhost:5173
 */
@SpringBootApplication
public class ProgramKerjaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramKerjaApplication.class, args);
    }

}
