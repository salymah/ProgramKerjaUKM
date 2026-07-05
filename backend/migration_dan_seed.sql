-- =====================================================================
-- SCRIPT MIGRASI & SEED DATA
-- Database : db_programkerja_ukm
-- Tujuan   : Menyatukan seluruh perubahan yang sudah disepakati selama
--            proses audit, agar bisa dijalankan sekali lewat phpMyAdmin.
--
-- CARA PAKAI:
-- 1. Buka phpMyAdmin -> pilih database db_programkerja_ukm
-- 2. Klik tab "SQL"
-- 3. Copy seluruh isi file ini, paste, lalu klik "Go"
-- =====================================================================

-- ---------------------------------------------------------------------
-- STEP 1: Perbaiki data role lama dari 'UMKM' (typo) menjadi 'UKM'
-- WAJIB dijalankan SEBELUM mengubah definisi enum di STEP 2,
-- supaya data lama tidak rusak/terpotong oleh MySQL.
-- ---------------------------------------------------------------------
UPDATE users SET role = 'UKM' WHERE role = 'UMKM';

-- ---------------------------------------------------------------------
-- STEP 2: Ubah definisi enum kolom role
-- Dari  : enum('UMKM','WKIII','PEMBINA')
-- Jadi  : enum('UKM','WKIII','PEMBINA')
-- ---------------------------------------------------------------------
ALTER TABLE users
  MODIFY COLUMN role ENUM('UKM', 'WKIII', 'PEMBINA') NOT NULL;

-- ---------------------------------------------------------------------
-- STEP 3: Seed 1 akun WK III pertama untuk keperluan login & testing API
-- Password sudah di-hash dengan BCrypt (cost factor 10), kompatibel
-- dengan BCryptPasswordEncoder yang dipakai backend Spring Security.
--
-- Username : admin
-- Password : admin123
--
-- Catatan: baris ini aman dijalankan berkali-kali karena memakai
-- INSERT IGNORE berdasarkan username yang unique.
-- ---------------------------------------------------------------------
INSERT IGNORE INTO users (nama, username, password, role, created_at)
VALUES (
  'Admin WK III',
  'admin',
  '$2b$10$PY5nHhqcqdrOGwe2/LBjdOnzU0suVJZ1zcITjr8uoYI9tDIx6T.kC',
  'WKIII',
  NOW()
);

-- ---------------------------------------------------------------------
-- STEP 4: Verifikasi hasil (jalankan terpisah untuk mengecek manual)
-- ---------------------------------------------------------------------
-- SELECT id, nama, username, role FROM users;
