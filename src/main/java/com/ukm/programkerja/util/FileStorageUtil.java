package com.ukm.programkerja.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Utility penanganan file upload (Proposal & Laporan).
 * Validasi format: hanya PDF/DOCX, maksimal 5MB (sesuai UC-03).
 */
@Component
public class FileStorageUtil {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "doc", "docx");
    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024; // 5MB

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Menyimpan file ke disk dalam sub-folder tertentu (misal "proposal"
     * atau "laporan"), mengembalikan nama file unik yang tersimpan.
     */
    public String store(MultipartFile file, String subFolder) {
        validate(file);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + "." + extension;

        try {
            Path targetDir = Paths.get(uploadDir, subFolder);
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Gagal menyimpan file: " + ex.getMessage(), ex);
        }
    }

    public Path getFilePath(String filename, String subFolder) {
        return Paths.get(uploadDir, subFolder).resolve(filename);
    }

    public void delete(String filename, String subFolder) {
        try {
            Files.deleteIfExists(getFilePath(filename, subFolder));
        } catch (IOException ex) {
            // Tidak melempar exception ke atas: kegagalan hapus file fisik
            // tidak boleh menggagalkan operasi hapus data di database.
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File tidak boleh kosong");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("Ukuran file melebihi batas maksimal 5MB");
        }

        String extension = getExtension(StringUtils.cleanPath(file.getOriginalFilename()));
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Format file tidak didukung. Hanya menerima: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("File tidak memiliki ekstensi yang valid");
        }
        return filename.substring(dotIndex + 1);
    }
}
