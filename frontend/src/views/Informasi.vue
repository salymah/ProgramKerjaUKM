<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
      <div>
        <h4 class="fw-bold mb-0">Informasi & Agenda Kampus</h4>
        <small class="text-muted">Pengumuman dan agenda kampus, mis. jadwal UAS atau akreditasi</small>
      </div>
      <button v-if="authStore.isWkIII" class="btn btn-primary" @click="openModal()">
        <i class="bi bi-plus-lg me-1"></i> Tambah Informasi
      </button>
    </div>

    <div v-if="store.loading" class="text-center py-5">
      <span class="spinner-border"></span>
    </div>
    <div v-else-if="store.list.length === 0" class="text-center text-muted py-5">
      <i class="bi bi-megaphone display-5 d-block mb-2"></i>
      Belum ada informasi atau agenda yang ditambahkan.
    </div>
    <div v-else class="row g-3">
      <div v-for="i in store.list" :key="i.id" class="col-md-6 col-lg-4">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body d-flex flex-column">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <span class="badge" :class="i.kategori === 'AGENDA' ? 'bg-primary' : 'bg-warning text-dark'">
                <i class="bi" :class="i.kategori === 'AGENDA' ? 'bi-calendar-event' : 'bi-megaphone'"></i>
                {{ i.kategori }}
              </span>
              <small class="text-muted">{{ formatDate(i.tanggal) }}</small>
            </div>
            <h6 class="fw-bold mb-1">{{ i.judul }}</h6>
            <p class="text-muted small mb-3 flex-grow-1">{{ i.isi }}</p>
            <div class="d-flex justify-content-between align-items-center">
              <small class="text-muted">Oleh {{ i.dibuatOleh || 'WK III' }}</small>
              <div v-if="authStore.isWkIII">
                <button class="btn btn-sm btn-outline-secondary me-1" @click="openModal(i)">
                  <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="handleDelete(i)">
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showModal" class="modal-backdrop-custom" @click.self="showModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-3">{{ form.id ? 'Edit' : 'Tambah' }} Informasi</h6>
          <div class="mb-2">
            <label class="form-label small">Judul</label>
            <input v-model="form.judul" class="form-control" placeholder="Contoh: Jadwal UAS Semester Genap" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Isi</label>
            <textarea v-model="form.isi" class="form-control" rows="3"></textarea>
          </div>
          <div class="row g-2">
            <div class="col-6">
              <label class="form-label small">Kategori</label>
              <select v-model="form.kategori" class="form-select">
                <option value="AGENDA">AGENDA</option>
                <option value="PENGUMUMAN">PENGUMUMAN</option>
              </select>
            </div>
            <div class="col-6">
              <label class="form-label small">Tanggal</label>
              <input v-model="form.tanggal" type="date" class="form-control" required />
            </div>
          </div>
          <div class="mt-3">
            <button class="btn btn-primary btn-sm" @click="handleSave">Simpan</button>
            <button class="btn btn-light btn-sm ms-2" @click="showModal = false">Batal</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Swal from 'sweetalert2'
import { useInformasiStore } from '@/stores/informasi'
import { useAuthStore } from '@/stores/auth'
import informasiService from '@/services/informasiService'

const store = useInformasiStore()
const authStore = useAuthStore()
const showModal = ref(false)

const form = reactive({ id: null, judul: '', isi: '', kategori: 'AGENDA', tanggal: '' })

const openModal = (i = null) => {
  form.id = i?.id || null
  form.judul = i?.judul || ''
  form.isi = i?.isi || ''
  form.kategori = i?.kategori || 'AGENDA'
  form.tanggal = i?.tanggal || new Date().toISOString().slice(0, 10)
  showModal.value = true
}

const handleSave = async () => {
  if (!form.judul.trim() || !form.tanggal) {
    Swal.fire('Lengkapi data', 'Judul dan tanggal wajib diisi', 'warning')
    return
  }
  try {
    if (form.id) {
      await informasiService.update(form.id, form)
    } else {
      await informasiService.create(form)
    }
    showModal.value = false
    await store.fetchAll()
    Swal.fire('Berhasil', 'Informasi tersimpan', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal menyimpan informasi', 'error')
  }
}

const handleDelete = async (i) => {
  const result = await Swal.fire({
    title: `Hapus "${i.judul}"?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Hapus',
    confirmButtonColor: '#dc3545',
  })
  if (result.isConfirmed) {
    await informasiService.delete(i.id)
    store.fetchAll()
  }
}

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('id-ID', { day: 'numeric', month: 'short', year: 'numeric' }) : '-')

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
  width: 420px;
  max-width: 90vw;
}
</style>
