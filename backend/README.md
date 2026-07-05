# Sistem Informasi Manajemen Program Kerja UKM — Backend

Backend Spring Boot untuk Sistem Informasi Manajemen Program Kerja
Unit Kegiatan Mahasiswa (UKM). Skripsi — STMIK Mardira Indonesia.

## Status Modul — BACKEND SELESAI ✅

| Modul | Status |
|---|---|
| Entity (User, Tempat, ProgramKerja, Proposal, Laporan, Catatan) | ✅ Selesai |
| Auth Module (Login + JWT) | ✅ Selesai (sudah diuji & berhasil) |
| User Module (CRUD) | ✅ Selesai |
| Tempat Module | ✅ Selesai |
| ProgramKerja Module (ajukan, verifikasi, statistik dashboard) | ✅ Selesai |
| Proposal Module (upload/download) | ✅ Selesai |
| Laporan Module (upload/download) | ✅ Selesai |
| Catatan Module | ✅ Selesai |

## Ringkasan Endpoint

| Method | Endpoint | Role | Keterangan |
|---|---|---|---|
| POST | `/api/auth/login` | Publik | Login, hasil JWT token |
| POST/PUT/DELETE/GET | `/api/users` | WKIII | Kelola akun pengguna |
| POST/PUT/DELETE | `/api/tempat` | WKIII | Kelola data tempat |
| GET | `/api/tempat` | Semua (login) | Lihat daftar tempat |
| POST | `/api/program-kerja` | UKM | Ajukan program kerja |
| PUT | `/api/program-kerja/{id}` | UKM (pemilik) | Ubah pengajuan |
| GET | `/api/program-kerja` | Semua (login) | UKM lihat milik sendiri, WKIII lihat semua |
| PATCH | `/api/program-kerja/{id}/verifikasi` | WKIII | Setuju / minta revisi |
| GET | `/api/program-kerja/statistik` | Semua (login) | Statistik dashboard |
| DELETE | `/api/program-kerja/{id}` | WKIII | Hapus program kerja |
| POST | `/api/proposal/program-kerja/{id}` | UKM | Upload proposal (multipart) |
| GET | `/api/proposal/{id}/download` | Semua (login) | Download proposal |
| POST | `/api/laporan/program-kerja/{id}` | UKM | Upload laporan (otomatis ubah status jadi SELESAI) |
| GET | `/api/laporan/{id}/download` | Semua (login) | Download laporan |
| POST | `/api/catatan/program-kerja/{id}` | WKIII, PEMBINA | Tambah catatan/saran |
| GET | `/api/catatan/program-kerja/{id}` | Semua (login) | Lihat catatan |

Dokumentasi interaktif lengkap (request/response body) tersedia di Swagger UI.

## Prasyarat

- Java 17 (sudah terbukti jalan dengan Zulu 17)
- XAMPP (MySQL aktif di port **3307**)
- VS Code dengan extension **Extension Pack for Java** dan **Spring Boot Extension Pack**
- Koneksi internet (untuk download dependency Maven saat pertama kali buka project)

## Langkah Setup (Urutan Wajib)

### 1. Jalankan migrasi database

Buka **phpMyAdmin** → pilih database `db_programkerja_ukm` → tab **SQL** →
copy-paste seluruh isi file `migration_dan_seed.sql` (ada di folder ini) →
klik **Go**.

Script ini akan:
- Memperbaiki typo `UMKM` → `UKM` pada kolom `role`
- Membuat 1 akun WK III pertama untuk login (`admin` / `admin123`)

### 2. Buka project di VS Code

`File → Open Folder` → pilih folder `program-kerja-ukm` ini (folder yang
berisi `pom.xml`, jangan folder induknya).

Tunggu sampai notifikasi "Importing Maven project..." selesai di pojok
kanan bawah sebelum menjalankan apa pun.

### 3. Jalankan aplikasi

Buka `src/main/java/com/ukm/programkerja/ProgramKerjaApplication.java`,
klik **Run** di atas method `main`.

Tunggu sampai muncul log:
```
Started ProgramKerjaApplication in X seconds
```

### 4. Tes API lewat Swagger

Buka browser: **http://localhost:8082/swagger-ui.html**

Login lewat `POST /api/auth/login`:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response akan berisi `token` JWT. Token ini dipakai untuk mengakses
endpoint lain yang membutuhkan autentikasi (klik tombol **Authorize**
di Swagger, masukkan `Bearer <token>`).

### 5. Alur tes modul lengkap (disarankan, berurutan)

1. Login sebagai `admin` (role WKIII)
2. `POST /api/tempat` — tambah minimal 1 data tempat
3. `POST /api/users` — buat 1 akun baru dengan role `UKM` (catat username/password)
4. Logout (hapus token Authorize), login ulang sebagai akun UKM tadi
5. `POST /api/program-kerja` — ajukan program kerja baru (status awal: MENUNGGU)
6. `POST /api/proposal/program-kerja/{id}` — upload file proposal (multipart, field `file`)
7. Login ulang sebagai `admin` (WKIII)
8. `PATCH /api/program-kerja/{id}/verifikasi` — set status `DISETUJUI`
9. Login ulang sebagai akun UKM
10. `POST /api/laporan/program-kerja/{id}` — upload laporan (status program kerja otomatis berubah jadi SELESAI)
11. Login ulang sebagai `admin`, coba `POST /api/catatan/program-kerja/{id}` — beri catatan
12. `GET /api/program-kerja/statistik` — cek angka dashboard berubah sesuai data di atas

## Konfigurasi Penting

File `src/main/resources/application.properties`:
- Database: `localhost:3307/db_programkerja_ukm`, user `root`, password kosong
- Backend port: `8082`
- Frontend (Vue.js) diasumsikan di: `http://localhost:5173`
- `ddl-auto=validate` — backend TIDAK akan mengubah struktur tabel
  secara otomatis. Semua perubahan skema wajib lewat SQL manual yang
  diaudit dulu.

## Jika Terjadi Error

Jangan langsung cari solusi acak. Kirim **seluruh isi log terminal**
(bukan screenshot browser) untuk diaudit terlebih dahulu.
