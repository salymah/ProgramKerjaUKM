<template>
  <div class="login-page">
    <div class="row g-0 min-vh-100">
      <!-- Panel kiri: ilustrasi -->
      <div class="col-lg-6 d-none d-lg-flex left-panel position-relative overflow-hidden">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>

        <div class="position-relative w-100 d-flex flex-column align-items-center justify-content-center text-center px-5 py-5">
          <img :src="logo" alt="Logo STMIK Mardira Indonesia" class="logo-hero mb-4" />
          <h2 class="fw-bold text-primary mb-2">Program Kerja UKM</h2>
          <p class="text-secondary mb-1 fw-semibold">
            Sistem Informasi Pengelolaan Program Kerja
          </p>
          <p class="text-muted mb-1">Unit Kegiatan Mahasiswa</p>
          <hr class="my-3 w-25" />
          <p class="text-muted mb-4" style="max-width: 420px;">
            Kelola, ajukan, dan pantau program kerja UKM dengan mudah, transparan, dan
            terstruktur demi kemajuan bersama.
          </p>

          <div class="d-flex gap-3 illustration-cards">
            <div class="float-card float-card-1">
              <i class="bi bi-bar-chart-fill text-primary fs-4"></i>
            </div>
            <div class="float-card float-card-2">
              <i class="bi bi-check2-circle text-success fs-4"></i>
            </div>
            <div class="float-card float-card-3">
              <i class="bi bi-chat-square-text-fill text-warning fs-4"></i>
            </div>
          </div>
        </div>
      </div>

      <!-- Panel kanan: form login -->
      <div class="col-lg-6 d-flex align-items-center justify-content-center bg-white">
        <div style="width: 380px; max-width: 90%;">
          <div class="text-center mb-4">
            <img :src="logo" alt="Logo STMIK Mardira Indonesia" class="logo-form mb-3" />
            <h4 class="fw-bold mb-1">Selamat Datang Kembali</h4>
            <small class="text-muted">Silakan masuk ke akun Anda untuk melanjutkan</small>
          </div>

          <form @submit.prevent="handleLogin">
            <div class="mb-3">
              <label class="form-label small fw-semibold">Username</label>
              <div class="input-group">
                <span class="input-group-text bg-light border-end-0">
                  <i class="bi bi-person text-muted"></i>
                </span>
                <input
                  v-model="username"
                  type="text"
                  class="form-control border-start-0 ps-0"
                  placeholder="Masukkan username"
                  required
                />
              </div>
            </div>

            <div class="mb-2">
              <label class="form-label small fw-semibold">Password</label>
              <div class="input-group">
                <span class="input-group-text bg-light border-end-0">
                  <i class="bi bi-lock text-muted"></i>
                </span>
                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  class="form-control border-start-0 border-end-0 ps-0"
                  placeholder="Masukkan password"
                  required
                />
                <span class="input-group-text bg-light border-start-0" style="cursor: pointer;" @click="showPassword = !showPassword">
                  <i class="bi" :class="showPassword ? 'bi-eye-slash text-muted' : 'bi-eye text-muted'"></i>
                </span>
              </div>
            </div>

            <div class="d-flex justify-content-between align-items-center mb-3">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="ingatSaya" />
                <label class="form-check-label small text-muted" for="ingatSaya">Ingat saya</label>
              </div>
              <a href="#" class="small text-decoration-none" @click.prevent="showLupaModal = true">Lupa password?</a>
            </div>

            <div v-if="errorMsg" class="alert alert-danger py-2 small">{{ errorMsg }}</div>

            <button type="submit" class="btn btn-primary w-100 py-2" :disabled="loading">
              <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
              <i v-else class="bi bi-box-arrow-in-right me-1"></i>
              Masuk
            </button>
          </form>

          <p class="text-center text-muted small mt-4 mb-0">
            &copy; {{ new Date().getFullYear() }} STMIK Mardira Indonesia
          </p>
        </div>
      </div>
    </div>

    <!-- Modal Lupa Password -->
    <div v-if="showLupaModal" class="modal-backdrop-custom" @click.self="showLupaModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-1">Lupa Password</h6>
          <p class="text-muted small mb-3">
            Sistem ini belum memiliki infrastruktur email, jadi permintaan Anda akan
            ditinjau langsung oleh WK III. Masukkan username akun Anda dan alasan singkat,
            lalu hubungi WK III untuk konfirmasi.
          </p>
          <div class="mb-2">
            <label class="form-label small">Username</label>
            <input v-model="lupaUsername" class="form-control" placeholder="Username akun Anda" />
          </div>
          <div class="mb-3">
            <label class="form-label small">Alasan (opsional)</label>
            <textarea v-model="lupaAlasan" class="form-control" rows="2" placeholder="Contoh: lupa password sejak lama"></textarea>
          </div>
          <button class="btn btn-primary btn-sm" :disabled="lupaLoading" @click="handleLupaPassword">
            <span v-if="lupaLoading" class="spinner-border spinner-border-sm me-1"></span>
            Kirim Permintaan
          </button>
          <button class="btn btn-light btn-sm ms-2" @click="showLupaModal = false">Batal</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Swal from 'sweetalert2'
import logo from '@/assets/logo-stmik.png'
import authService from '@/services/authService'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')
const showPassword = ref(false)

const showLupaModal = ref(false)
const lupaUsername = ref('')
const lupaAlasan = ref('')
const lupaLoading = ref(false)

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    await authStore.login(username.value, password.value)
    router.push('/dashboard')
  } catch (err) {
    errorMsg.value =
      err.response?.data?.message || 'Username atau password salah. Silakan coba lagi.'
  } finally {
    loading.value = false
  }
}

const handleLupaPassword = async () => {
  if (!lupaUsername.value.trim()) {
    Swal.fire('Lengkapi data', 'Username wajib diisi', 'warning')
    return
  }
  lupaLoading.value = true
  try {
    await authService.lupaPassword(lupaUsername.value, lupaAlasan.value)
    showLupaModal.value = false
    lupaUsername.value = ''
    lupaAlasan.value = ''
    Swal.fire(
      'Permintaan Terkirim',
      'Permintaan reset password Anda telah dikirim dan akan ditinjau oleh WK III.',
      'success'
    )
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal mengirim permintaan', 'error')
  } finally {
    lupaLoading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background-color: #ffffff;
}

.left-panel {
  background: linear-gradient(135deg, #eaf1ff 0%, #dbe8ff 100%);
}

.logo-hero {
  width: 130px;
  height: auto;
  filter: drop-shadow(0 6px 14px rgba(13, 110, 253, 0.25));
}

.logo-form {
  width: 80px;
  height: auto;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  border: 2px solid rgba(13, 110, 253, 0.15);
}
.deco-1 {
  width: 90px;
  height: 90px;
  top: 12%;
  left: 8%;
}
.deco-2 {
  width: 40px;
  height: 40px;
  bottom: 18%;
  left: 14%;
  background: rgba(13, 110, 253, 0.12);
}
.deco-3 {
  width: 60px;
  height: 60px;
  top: 20%;
  right: 10%;
  background: rgba(13, 110, 253, 0.08);
}

.float-card {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(13, 110, 253, 0.15);
}
.float-card-1 {
  margin-top: 14px;
}
.float-card-2 {
  margin-top: -10px;
}
.float-card-3 {
  margin-top: 20px;
}
.modal-backdrop-custom {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1050;
}
.modal-box {
  width: 400px;
  max-width: 90vw;
}
</style>
