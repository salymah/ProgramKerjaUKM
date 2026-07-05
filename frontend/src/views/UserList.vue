<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
      <h4 class="fw-bold mb-0">Kelola Pengguna</h4>
      <button class="btn btn-primary" @click="openModal()">
        <i class="bi bi-plus-lg me-1"></i> Tambah Pengguna
      </button>
    </div>

    <!-- Permintaan Reset Password -->
    <div class="card border-0 shadow-sm mb-3" v-if="resetRequests.length > 0">
      <div class="card-header bg-white fw-semibold d-flex align-items-center">
        <i class="bi bi-key text-danger me-2"></i> Permintaan Reset Password
        <span class="badge bg-danger ms-2">{{ resetRequests.length }}</span>
      </div>
      <ul class="list-group list-group-flush">
        <li
          v-for="r in resetRequests"
          :key="r.id"
          class="list-group-item d-flex justify-content-between align-items-center flex-wrap gap-2"
        >
          <div>
            <div class="fw-semibold small">{{ r.namaUser }} ({{ r.username }})</div>
            <div class="text-muted small">{{ r.alasan || 'Tidak ada alasan disertakan' }}</div>
            <small class="text-muted">{{ formatDate(r.createdAt) }}</small>
          </div>
          <button class="btn btn-sm btn-outline-danger" @click="openResetModal(r)">
            <i class="bi bi-key-fill me-1"></i> Atur Password Baru
          </button>
        </li>
      </ul>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="table-responsive">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr>
              <th>Nama</th>
              <th>Username</th>
              <th>Role</th>
              <th>UKM Terhubung</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="store.loading">
              <td colspan="5" class="text-center py-4">
                <span class="spinner-border spinner-border-sm"></span>
              </td>
            </tr>
            <tr v-for="u in store.list" :key="u.id">
              <td>{{ u.nama }}</td>
              <td>{{ u.username }}</td>
              <td><span class="badge bg-secondary">{{ u.role }}</span></td>
              <td>{{ u.namaUkm || '-' }}</td>
              <td>
                <button class="btn btn-sm btn-outline-secondary me-1" @click="openModal(u)">
                  <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="handleDelete(u)">
                  <i class="bi bi-trash"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal tambah/edit pengguna -->
    <div v-if="showModal" class="modal-backdrop-custom" @click.self="showModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-3">{{ form.id ? 'Edit' : 'Tambah' }} Pengguna</h6>
          <div class="mb-2">
            <label class="form-label small">Nama</label>
            <input v-model="form.nama" class="form-control" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Username</label>
            <input v-model="form.username" class="form-control" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Password {{ form.id ? '(kosongkan jika tidak diubah)' : '' }}</label>
            <input v-model="form.password" type="password" class="form-control" />
          </div>
          <div class="mb-2">
            <label class="form-label small">Role</label>
            <select v-model="form.role" class="form-select">
              <option value="UKM">UKM</option>
              <option value="WKIII">WK III</option>
              <option value="PEMBINA">PEMBINA</option>
            </select>
          </div>
          <div class="mb-3" v-if="form.role === 'UKM'">
            <label class="form-label small">UKM Terhubung</label>
            <select v-model="form.ukmId" class="form-select">
              <option :value="null">-- Pilih UKM --</option>
              <option v-for="u in ukmStore.list" :key="u.id" :value="u.id">{{ u.namaUkm }}</option>
            </select>
            <small class="text-muted">Akun ini akan mewakili UKM yang dipilih saat mengajukan program kerja.</small>
          </div>
          <button class="btn btn-primary btn-sm" @click="handleSave">Simpan</button>
          <button class="btn btn-light btn-sm ms-2" @click="showModal = false">Batal</button>
        </div>
      </div>
    </div>

    <!-- Modal atur password baru (reset password) -->
    <div v-if="showResetModal" class="modal-backdrop-custom" @click.self="showResetModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-1">Atur Password Baru</h6>
          <p class="text-muted small mb-3">
            Untuk {{ resetTarget?.namaUser }} ({{ resetTarget?.username }})
          </p>
          <div class="mb-3">
            <label class="form-label small">Password Baru</label>
            <input v-model="passwordBaru" type="password" class="form-control" minlength="6" required />
          </div>
          <button class="btn btn-danger btn-sm" @click="handleProsesReset">Simpan Password Baru</button>
          <button class="btn btn-light btn-sm ms-2" @click="showResetModal = false">Batal</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/user'
import { useUkmStore } from '@/stores/ukm'
import userService from '@/services/userService'
import authService from '@/services/authService'

const store = useUserStore()
const ukmStore = useUkmStore()
const showModal = ref(false)

const form = reactive({ id: null, nama: '', username: '', password: '', role: 'UKM', ukmId: null })

const resetRequests = ref([])
const showResetModal = ref(false)
const resetTarget = ref(null)
const passwordBaru = ref('')

const openModal = (u = null) => {
  form.id = u?.id || null
  form.nama = u?.nama || ''
  form.username = u?.username || ''
  form.password = ''
  form.role = u?.role || 'UKM'
  form.ukmId = u?.ukmId || null
  showModal.value = true
}

const handleSave = async () => {
  try {
    const payload = { ...form }
    if (form.id && !payload.password) delete payload.password
    if (payload.role !== 'UKM') payload.ukmId = null
    if (form.id) {
      await userService.update(form.id, payload)
    } else {
      await userService.create(payload)
    }
    showModal.value = false
    await store.fetchAll()
    Swal.fire('Berhasil', 'Data pengguna tersimpan', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal menyimpan data', 'error')
  }
}

const handleDelete = async (u) => {
  const result = await Swal.fire({
    title: `Hapus pengguna "${u.nama}"?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Hapus',
    confirmButtonColor: '#dc3545',
  })
  if (result.isConfirmed) {
    await userService.delete(u.id)
    store.fetchAll()
  }
}

const loadResetRequests = async () => {
  try {
    const res = await authService.getResetRequests()
    resetRequests.value = (res.data.data || []).filter((r) => r.status === 'PENDING')
  } catch (err) {
    // abaikan jika gagal memuat, tidak kritikal untuk halaman ini
  }
}

const openResetModal = (r) => {
  resetTarget.value = r
  passwordBaru.value = ''
  showResetModal.value = true
}

const handleProsesReset = async () => {
  if (!passwordBaru.value || passwordBaru.value.length < 6) {
    Swal.fire('Password Terlalu Pendek', 'Password baru minimal 6 karakter', 'warning')
    return
  }
  try {
    await authService.prosesResetPassword(resetTarget.value.id, passwordBaru.value)
    showResetModal.value = false
    await loadResetRequests()
    Swal.fire('Berhasil', 'Password pengguna berhasil diatur ulang', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal mengatur ulang password', 'error')
  }
}

const formatDate = (d) => (d ? new Date(d).toLocaleString('id-ID') : '-')

onMounted(() => {
  store.fetchAll()
  ukmStore.fetchAll()
  loadResetRequests()
})
</script>

<style scoped>
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
  width: 420px;
  max-width: 90vw;
}
</style>
