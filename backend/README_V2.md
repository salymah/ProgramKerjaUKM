# Program Kerja UKM - Backend v2

## LANGKAH WAJIB SEBELUM MENJALANKAN

### 1. Jalankan migrasi SQL di phpMyAdmin
Buka file `migration_v2_fitur_baru.sql` di phpMyAdmin, pilih database `db_programkerja_ukm`, lalu jalankan.

File ini menambahkan 5 perubahan struktur database:
- Tabel `ukm` (data organisasi mahasiswa)
- Kolom `users.ukm_id` (relasi akun login ke tabel ukm)
- Kolom `program_kerja.tanggal_kegiatan` (untuk cek bentrok jadwal tempat)
- Tabel `reset_password_request` (fitur lupa password)
- Tabel `informasi` (agenda/pengumuman kampus)

### 2. Jalankan backend di VS Code
Buka folder ini, tunggu Maven download dependency, lalu Run `ProgramKerjaApplication.java`.

### 3. Swagger UI
http://localhost:8082/swagger-ui.html

---

## Fitur Baru di v2

| Fitur | Endpoint |
|---|---|
| Kelola Data UKM (UC-08) | GET/POST/PUT/DELETE /api/ukm |
| Lupa Password | POST /api/auth/lupa-password (publik) |
| Daftar & Proses Reset Password (WK III) | GET /api/auth/reset-requests, PATCH /api/auth/reset-requests/{id} |
| Cek Bentrok Jadwal Tempat | Otomatis saat POST/PUT /api/program-kerja |
| Notifikasi per Role | GET /api/notifikasi |
| Informasi & Agenda Kampus | GET/POST/PUT/DELETE /api/informasi |
| Relasi User → UKM | Field ukmId di UserCreateRequest/UserUpdateRequest |
