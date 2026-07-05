-- ============================================================
-- MIGRATION V2 - Penambahan Fitur Baru
-- Database: db_programkerja_ukm
-- WAJIB dijalankan SEBELUM menjalankan backend versi baru ini,
-- karena Entity Java baru memerlukan kolom/tabel berikut.
-- ============================================================

-- 1. Tabel UKM (organisasi mahasiswa) -----------------------------
CREATE TABLE IF NOT EXISTS ukm (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_ukm VARCHAR(100) NOT NULL,
    bidang VARCHAR(100) NOT NULL,
    deskripsi TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'AKTIF',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed 8 UKM yang sudah ada di kampus
INSERT INTO ukm (nama_ukm, bidang, deskripsi, status) VALUES
('Komunitas Seni Teater', 'Seni & Teater', 'UKM yang bergerak di bidang seni peran, naskah, dan pementasan teater kampus.', 'AKTIF'),
('CSA (Computer Science & Art)', 'Komputer & Kreativitas', 'UKM yang bergerak di bidang komputer, coding, dan pengembangan kreativitas digital mahasiswa.', 'AKTIF'),
('UDMI (Unit Dakwah Mahasiswa Islam)', 'Keimanan & Keislaman', 'UKM yang bergerak di bidang pembinaan keimanan dan syiar keislaman mahasiswa.', 'AKTIF'),
('Wallet (Wanderlust Adventure)', 'Pecinta Alam & Pendakian', 'UKM yang bergerak di bidang kegiatan alam bebas dan pendakian gunung.', 'AKTIF'),
('PMK (Persatuan Mahasiswa Kristen)', 'Kerohanian Kristen', 'UKM yang bergerak di bidang pembinaan kerohanian dan persekutuan mahasiswa Kristen.', 'AKTIF'),
('BEM (Badan Eksekutif Mahasiswa)', 'Organisasi & Kepemimpinan', 'Lembaga eksekutif tertinggi mahasiswa yang mengoordinasikan kegiatan kemahasiswaan kampus.', 'AKTIF'),
('HIMA SI (Himpunan Mahasiswa Sistem Informasi)', 'Akademik Sistem Informasi', 'Himpunan mahasiswa program studi Sistem Informasi.', 'AKTIF'),
('HIMA KA (Himpunan Mahasiswa Komputerisasi Akuntansi)', 'Akademik Komputerisasi Akuntansi', 'Himpunan mahasiswa program studi Komputerisasi Akuntansi.', 'AKTIF');

-- 2. Relasi user (role UKM) ke tabel ukm ---------------------------
ALTER TABLE users ADD COLUMN IF NOT EXISTS ukm_id BIGINT NULL;
ALTER TABLE users ADD CONSTRAINT fk_users_ukm
    FOREIGN KEY (ukm_id) REFERENCES ukm(id) ON DELETE SET NULL;

-- (Opsional) Hubungkan akun UKM contoh yang sudah ada ke salah satu UKM,
-- sesuaikan id user dan id ukm dengan data aktual kamu:
-- UPDATE users SET ukm_id = 2 WHERE username = 'ukm1';

-- 3. Kolom tanggal kegiatan di program_kerja (untuk cek bentrok jadwal tempat) --
ALTER TABLE program_kerja ADD COLUMN IF NOT EXISTS tanggal_kegiatan DATE NULL;

-- 4. Tabel permintaan reset password (fitur "Lupa Password") ------
CREATE TABLE IF NOT EXISTS reset_password_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    alasan TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    diproses_at DATETIME NULL,
    CONSTRAINT fk_reset_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Tabel informasi/agenda kampus ---------------------------------
CREATE TABLE IF NOT EXISTS informasi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    judul VARCHAR(200) NOT NULL,
    isi TEXT,
    kategori VARCHAR(30) NOT NULL DEFAULT 'PENGUMUMAN',
    tanggal DATE NOT NULL,
    dibuat_oleh BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_informasi_user FOREIGN KEY (dibuat_oleh) REFERENCES users(id) ON DELETE SET NULL
);

-- Seed contoh informasi/agenda kampus
INSERT INTO informasi (judul, isi, kategori, tanggal) VALUES
('Jadwal UAS Semester Genap', 'Ujian Akhir Semester genap akan dilaksanakan mulai tanggal yang ditentukan. Mahasiswa wajib melunasi administrasi sebelum mengikuti ujian.', 'AGENDA', CURDATE()),
('Visitasi Akreditasi Program Studi', 'Kampus akan menerima kunjungan asesor akreditasi. Seluruh UKM diharapkan menyiapkan dokumentasi kegiatan terbaik untuk ditampilkan.', 'AGENDA', CURDATE()),
('Batas Akhir Pengajuan Program Kerja Semester Ini', 'UKM diharapkan mengajukan seluruh program kerja semester ini sebelum batas waktu yang ditentukan agar dapat diproses tepat waktu.', 'PENGUMUMAN', CURDATE());
