<template>
  <div>
    <h4 class="fw-bold mb-3">Pengaturan</h4>
    <div class="card border-0 shadow-sm" style="max-width: 500px;">
      <div class="card-body">
        <h6 class="fw-semibold mb-3">Ubah Password</h6>
        <form @submit.prevent="handleChangePassword">
          <div class="mb-3">
            <label class="form-label">Password Baru</label>
            <input v-model="passwordBaru" type="password" class="form-control" required minlength="6" />
          </div>
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
            Simpan Perubahan
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Swal from 'sweetalert2'
import { useAuthStore } from '@/stores/auth'
import userService from '@/services/userService'

const authStore = useAuthStore()
const passwordBaru = ref('')
const loading = ref(false)

const handleChangePassword = async () => {
  loading.value = true
  try {
    await userService.update(authStore.user.id, {
      nama: authStore.user.nama,
      username: authStore.user.username,
      role: authStore.user.role,
      password: passwordBaru.value,
    })
    passwordBaru.value = ''
    Swal.fire('Berhasil', 'Password berhasil diubah', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal mengubah password', 'error')
  } finally {
    loading.value = false
  }
}
</script>
