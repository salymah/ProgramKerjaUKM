<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
      <div>
        <h4 class="fw-bold mb-0">Kelola Data UKM</h4>
        <small class="text-muted">Daftar organisasi mahasiswa (UKM/HIMA/BEM) yang terdaftar di kampus</small>
      </div>
      <button v-if="authStore.isWkIII" class="btn btn-primary" @click="openModal()">
        <i class="bi bi-plus-lg me-1"></i> Tambah UKM
      </button>
    </div>

    <div class="row g-3">
      <div v-if="store.loading" class="col-12 text-center py-5">
        <span class="spinner-border"></span>
      </div>
      <div v-else-if="store.list.length === 0" class="col-12 text-center text-muted py-5">
        Belum ada data UKM
      </div>
      <div v-for="u in store.list" :key="u.id" class="col-md-6 col-lg-4">
        <div class="card border-0 shadow-sm h-100 ukm-card">
          <div class="card-body d-flex flex-column">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="ukm-icon" :class="iconBg(u.bidang)">
                <i class="bi" :class="iconFor(u.bidang)"></i>
              </div>
              <span class="badge" :class="u.status === 'AKTIF' ? 'bg-success' : 'bg-secondary'">
                {{ u.status }}
              </span>
            </div>
            <h6 class="fw-bold mb-1">{{ u.namaUkm }}</h6>
            <div class="small text-primary fw-semibold mb-2">{{ u.bidang }}</div>
            <p class="text-muted small mb-3 flex-grow-1">{{ u.deskripsi || 'Belum ada deskripsi.' }}</p>
            <div class="d-flex justify-content-between align-items-center">
              <small class="text-muted"><i class="bi bi-people me-1"></i>{{ u.jumlahAnggota }} akun terhubung</small>
              <div v-if="authStore.isWkIII">
                <button class="btn btn-sm btn-outline-secondary me-1" @click="openModal(u)">
                  <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="handleDelete(u)">
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal tambah/edit -->
    <div v-if="showModal" class="modal-backdrop-custom" @click.self="showModal = false">
      <div class="modal-box card shadow">
        <div class="card-body">
          <h6 class="fw-semibold mb-3">{{ form.id ? 'Edit' : 'Tambah' }} UKM</h6>
          <div class="mb-2">
            <label class="form-label small">Nama UKM</label>
            <input v-model="form.namaUkm" class="form-control" placeholder="Contoh: BEM (Badan Eksekutif Mahasiswa)" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Bidang</label>
            <input v-model="form.bidang" class="form-control" placeholder="Contoh: Organisasi & Kepemimpinan" required />
          </div>
          <div class="mb-2">
            <label class="form-label small">Deskripsi</label>
            <textarea v-model="form.deskripsi" class="form-control" rows="3"></textarea>
          </div>
          <div class="mb-3">
            <label class="form-label small">Status</label>
            <select v-model="form.status" class="form-select">
              <option value="AKTIF">AKTIF</option>
              <option value="TIDAK_AKTIF">TIDAK AKTIF</option>
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
import { useUkmStore } from '@/stores/ukm'
import { useAuthStore } from '@/stores/auth'
import ukmService from '@/services/ukmService'

const store = useUkmStore()
const authStore = useAuthStore()
const showModal = ref(false)

const form = reactive({ id: null, namaUkm: '', bidang: '', deskripsi: '', status: 'AKTIF' })

const openModal = (u = null) => {
  form.id = u?.id || null
  form.namaUkm = u?.namaUkm || ''
  form.bidang = u?.bidang || ''
  form.deskripsi = u?.deskripsi || ''
  form.status = u?.status || 'AKTIF'
  showModal.value = true
}

const handleSave = async () => {
  if (!form.namaUkm.trim() || !form.bidang.trim()) {
    Swal.fire('Lengkapi data', 'Nama UKM dan bidang wajib diisi', 'warning')
    return
  }
  try {
    if (form.id) {
      await ukmService.update(form.id, form)
    } else {
      await ukmService.create(form)
    }
    showModal.value = false
    await store.fetchAll()
    Swal.fire('Berhasil', 'Data UKM tersimpan', 'success')
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal menyimpan data UKM', 'error')
  }
}

const handleDelete = async (u) => {
  const result = await Swal.fire({
    title: `Hapus "${u.namaUkm}"?`,
    text: 'Data UKM yang dihapus tidak dapat dikembalikan.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Hapus',
    confirmButtonColor: '#dc3545',
  })
  if (result.isConfirmed) {
    try {
      await ukmService.delete(u.id)
      store.fetchAll()
    } catch (err) {
      Swal.fire('Gagal', err.response?.data?.message || 'Gagal menghapus data UKM', 'error')
    }
  }
}

const iconFor = (bidang = '') => {
  const b = bidang.toLowerCase()
  if (b.includes('seni') || b.includes('teater')) return 'bi-mask'
  if (b.includes('komputer') || b.includes('kreativ')) return 'bi-code-slash'
  if (b.includes('islam') || b.includes('iman')) return 'bi-moon-stars'
  if (b.includes('alam') || b.includes('gunung') || b.includes('pendakian')) return 'bi-tree'
  if (b.includes('kristen') || b.includes('rohani')) return 'bi-heart'
  if (b.includes('organisasi') || b.includes('kepemimpinan')) return 'bi-bank'
  if (b.includes('sistem informasi')) return 'bi-diagram-3'
  if (b.includes('akuntansi')) return 'bi-calculator'
  return 'bi-people-fill'
}

const iconBg = (bidang = '') => {
  const b = bidang.toLowerCase()
  if (b.includes('seni') || b.includes('teater')) return 'bg-danger-subtle text-danger'
  if (b.includes('komputer') || b.includes('kreativ')) return 'bg-primary-subtle text-primary'
  if (b.includes('islam') || b.includes('iman')) return 'bg-success-subtle text-success'
  if (b.includes('alam') || b.includes('gunung')) return 'bg-warning-subtle text-warning'
  if (b.includes('kristen') || b.includes('rohani')) return 'bg-info-subtle text-info'
  if (b.includes('organisasi')) return 'bg-dark-subtle text-dark'
  return 'bg-secondary-subtle text-secondary'
}

onMounted(() => store.fetchAll())
</script>

<style scoped>
.ukm-card {
  transition: transform 0.15s ease;
}
.ukm-card:hover {
  transform: translateY(-2px);
}
.ukm-icon {
  width: 42px;
  height: 42px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
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
  width: 420px;
  max-width: 90vw;
}
</style>
