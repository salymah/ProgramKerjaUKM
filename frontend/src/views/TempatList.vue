<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h4 class="fw-bold mb-0">Kelola Tempat</h4>
      <button v-if="authStore.isWkIII" class="btn btn-primary" @click="openModal()">
        <i class="bi bi-plus-lg me-1"></i> Tambah Tempat
      </button>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="table-responsive">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr>
              <th>Nama Tempat</th>
              <th>Alamat</th>
              <th>Status</th>
              <th v-if="authStore.isWkIII"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="store.loading">
              <td colspan="4" class="text-center py-4">
                <span class="spinner-border spinner-border-sm"></span>
              </td>
            </tr>
            <tr v-else-if="store.list.length === 0">
              <td colspan="4" class="text-center text-muted py-4">Belum ada data tempat</td>
            </tr>
            <tr v-for="t in store.list" :key="t.id">
              <td>{{ t.namaTempat }}</td>
              <td>{{ t.alamat }}</td>
              <td>{{ t.status || '-' }}</td>
              <td v-if="authStore.isWkIII">
                <button class="btn btn-sm btn-outline-secondary me-1" @click="openModal(t)">
                  <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="handleDelete(t)">
                  <i class="bi bi-trash"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal sederhana -->
    <div v-if="showModal" class="modal-backdrop-custom" @click.self="showModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-3">{{ form.id ? 'Edit' : 'Tambah' }} Tempat</h6>
          <div class="mb-2">
            <label class="form-label small">Nama Tempat</label>
            <input v-model="form.namaTempat" class="form-control" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Alamat</label>
            <textarea v-model="form.alamat" class="form-control" rows="2"></textarea>
          </div>
          <div class="mb-3">
            <label class="form-label small">Status</label>
            <select v-model="form.status" class="form-select">
              <option value="TERSEDIA">TERSEDIA</option>
              <option value="TIDAK_TERSEDIA">TIDAK TERSEDIA</option>
            </select>
          </div>
          <button class="btn btn-primary btn-sm" @click="handleSave">Simpan</button>
          <button class="btn btn-light btn-sm ms-2" @click="showModal = false">Batal</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Swal from 'sweetalert2'
import { useTempatStore } from '@/stores/tempat'
import { useAuthStore } from '@/stores/auth'
import tempatService from '@/services/tempatService'

const store = useTempatStore()
const authStore = useAuthStore()
const showModal = ref(false)

const form = reactive({ id: null, namaTempat: '', alamat: '', status: 'TERSEDIA' })

const openModal = (t = null) => {
  form.id = t?.id || null
  form.namaTempat = t?.namaTempat || ''
  form.alamat = t?.alamat || ''
  form.status = t?.status || 'TERSEDIA'
  showModal.value = true
}

const handleSave = async () => {
  try {
    if (form.id) {
      await tempatService.update(form.id, form)
    } else {
      await tempatService.create(form)
    }
    showModal.value = false
    await store.fetchAll()
    Swal.fire('Berhasil', 'Data tempat tersimpan', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal menyimpan data', 'error')
  }
}

const handleDelete = async (t) => {
  const result = await Swal.fire({
    title: `Hapus "${t.namaTempat}"?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Hapus',
    confirmButtonColor: '#dc3545',
  })
  if (result.isConfirmed) {
    await tempatService.delete(t.id)
    store.fetchAll()
  }
}

onMounted(() => store.fetchAll())
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
  width: 400px;
  max-width: 90vw;
}
</style>
