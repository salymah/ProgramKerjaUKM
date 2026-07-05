# Frontend - Sistem Informasi Pengelolaan Program Kerja UKM

Vue.js 3 (Composition API) + Vite + Pinia + Vue Router + Axios + Bootstrap 5 + SweetAlert2.

## Persiapan

1. Pastikan backend Spring Boot sudah jalan di `http://localhost:8082`
   (base URL diset di `src/services/api.js` — ubah di sana kalau port/host backend kamu berbeda).
2. Install dependency:
   ```
   npm install
   ```
3. Jalankan dev server:
   ```
   npm run dev
   ```
4. Buka `http://localhost:5173`

## Login

Gunakan akun yang sudah di-seed di database backend, contoh:
- Username: `admin`
- Password: `admin123`
(role WKIII)

## Struktur Folder

```
src/
 ├── assets/          file statis
 ├── components/      komponen reusable (kosong, siap dipakai kalau perlu)
 ├── layouts/          MainLayout.vue (sidebar + navbar)
 ├── router/           index.js (route + guard auth & role)
 ├── services/         axios instance + service per modul (auth, user, tempat, programKerja, proposal, laporan, catatan)
 ├── stores/           Pinia store (auth, programKerja, tempat, user)
 └── views/            halaman per fitur
      ├── programKerja/  (List, Form ajukan, Detail)
      ├── Dashboard.vue
      ├── Login.vue
      ├── ProposalList.vue
      ├── LaporanList.vue
      ├── CatatanList.vue
      ├── TempatList.vue
      ├── UserList.vue
      ├── Profil.vue
      ├── Pengaturan.vue
      └── NotFound.vue
```

## Alur Akses per Role

- **UKM**: ajukan program kerja, upload proposal, upload laporan (setelah disetujui), lihat catatan dari WK III/Pembina.
- **WKIII**: lihat semua program kerja, verifikasi (setujui/revisi), kelola tempat, kelola pengguna, beri catatan.
- **PEMBINA**: lihat program kerja & laporan, beri catatan/saran (tanpa approve).

## Build Production

```
npm run build
```
Hasil build ada di folder `dist/`.

## Catatan Penting

- Token JWT disimpan di `localStorage` dan otomatis disisipkan ke setiap request via interceptor Axios.
- Kalau token expired/invalid (401), user otomatis di-logout dan diarahkan ke halaman login.
- Route guard di `router/index.js` membatasi akses halaman sesuai role (`meta.roles`), konsisten dengan `@PreAuthorize` di backend — tapi ini hanya proteksi UI, validasi keamanan utama tetap di backend.
