<template>
  <div>
    <h4 class="fw-bold mb-3">Ajukan Program Kerja</h4>
    <div class="card border-0 shadow-sm" style="max-width: 600px;">
      <div class="card-body">
        <form @submit.prevent="handleSubmit">
          <div class="mb-3">
            <label class="form-label">Nama Program</label>
            <input v-model="form.namaProgram" type="text" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Deskripsi</label>
            <textarea v-model="form.deskripsi" class="form-control" rows="4" required></textarea>
          </div>
          <div class="mb-3">
            <label class="form-label">Tempat</label>
            <select v-model="form.tempatId" class="form-select" required>
              <option value="" disabled>Pilih tempat</option>
              <option v-for="t in tempatStore.list" :key="t.id" :value="t.id">
                {{ t.namaTempat }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Tanggal Pelaksanaan Kegiatan</label>
            <input v-model="form.tanggalKegiatan" type="date" class="form-control" :min="today" required />
            <small class="text-muted">
              Dipakai untuk mengecek jadwal tempat agar tidak bentrok dengan program kerja UKM lain.
            </small>
          </div>
          <div v-if="errorMsg" class="alert alert-danger py-2 small">{{ errorMsg }}</div>
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
            Ajukan
          </button>
          <router-link to="/program-kerja" class="btn btn-light ms-2">Batal</router-link>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import programKerjaService from '@/services/programKerjaService'
import { useTempatStore } from '@/stores/tempat'

const router = useRouter()
const tempatStore = useTempatStore()
const loading = ref(false)
const errorMsg = ref('')

const form = reactive({
  namaProgram: '',
  deskripsi: '',
  tempatId: '',
  tanggalKegiatan: '',
})

const today = new Date().toISOString().slice(0, 10)

const handleSubmit = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    await programKerjaService.create(form)
    await Swal.fire('Berhasil', 'Program kerja berhasil diajukan', 'success')
    router.push('/program-kerja')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || 'Gagal mengajukan program kerja'
  } finally {
    loading.value = false
  }
}

onMounted(() => tempatStore.fetchAll())
</script>
