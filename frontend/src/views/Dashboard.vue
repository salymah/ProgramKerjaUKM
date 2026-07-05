<template>
  <div>
    <h4 class="fw-bold mb-3">Dashboard</h4>

    <!-- Banner informasi -->
    <div class="card border-0 shadow-sm mb-3 banner-card">
      <div class="card-body d-flex align-items-center justify-content-between flex-wrap">
        <div class="d-flex align-items-start">
          <div class="banner-icon me-3">
            <i class="bi bi-info-circle-fill"></i>
          </div>
          <div>
            <h6 class="fw-bold mb-1">{{ bannerTitle }}</h6>
            <p class="text-muted small mb-0" style="max-width: 560px;">{{ bannerText }}</p>
          </div>
        </div>
        <i class="bi bi-rocket-takeoff banner-deco d-none d-lg-block"></i>
      </div>
    </div>

    <div class="row g-3 mb-4">
      <div class="col-md-3 col-6">
        <div class="card border-0 shadow-sm h-100 stat-card">
          <div class="card-body">
            <div class="d-flex align-items-center justify-content-between">
              <div class="text-muted small">Program Kerja</div>
              <div class="stat-icon bg-primary-subtle text-primary"><i class="bi bi-kanban"></i></div>
            </div>
            <div class="fs-3 fw-bold mt-2">{{ statistik.totalProgramKerja ?? 0 }}</div>
            <small class="text-muted">Total Program Kerja</small>
          </div>
        </div>
      </div>
      <div class="col-md-3 col-6">
        <div class="card border-0 shadow-sm h-100 stat-card">
          <div class="card-body">
            <div class="d-flex align-items-center justify-content-between">
              <div class="text-muted small">Diajukan</div>
              <div class="stat-icon bg-warning-subtle text-warning"><i class="bi bi-send"></i></div>
            </div>
            <div class="fs-3 fw-bold mt-2">{{ statistik.diajukan ?? 0 }}</div>
            <small class="text-muted">Menunggu Persetujuan</small>
          </div>
        </div>
      </div>
      <div class="col-md-3 col-6">
        <div class="card border-0 shadow-sm h-100 stat-card">
          <div class="card-body">
            <div class="d-flex align-items-center justify-content-between">
              <div class="text-muted small">Disetujui WK III</div>
              <div class="stat-icon bg-success-subtle text-success"><i class="bi bi-check-circle"></i></div>
            </div>
            <div class="fs-3 fw-bold mt-2">{{ statistik.disetujui ?? 0 }}</div>
            <small class="text-muted">Telah Disetujui</small>
          </div>
        </div>
      </div>
      <div class="col-md-3 col-6">
        <div class="card border-0 shadow-sm h-100 stat-card">
          <div class="card-body">
            <div class="d-flex align-items-center justify-content-between">
              <div class="text-muted small">Revisi</div>
              <div class="stat-icon bg-danger-subtle text-danger"><i class="bi bi-arrow-counterclockwise"></i></div>
            </div>
            <div class="fs-3 fw-bold mt-2">{{ statistik.revisi ?? 0 }}</div>
            <small class="text-muted">Perlu Perbaikan</small>
          </div>
        </div>
      </div>
      <div class="col-md-3 col-6">
        <div class="card border-0 shadow-sm h-100 stat-card">
          <div class="card-body">
            <div class="d-flex align-items-center justify-content-between">
              <div class="text-muted small">Selesai</div>
              <div class="stat-icon bg-secondary-subtle text-secondary"><i class="bi bi-flag"></i></div>
            </div>
            <div class="fs-3 fw-bold mt-2">{{ statistik.selesai ?? 0 }}</div>
            <small class="text-muted">Program Selesai</small>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-3 mb-4">
      <div class="col-lg-7">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
            <span><i class="bi bi-megaphone text-warning me-2"></i>Informasi & Agenda Kampus</span>
            <router-link to="/informasi" class="small text-decoration-none">Lihat semua</router-link>
          </div>
          <div class="card-body">
            <div v-if="informasiLoading" class="text-center py-3">
              <span class="spinner-border spinner-border-sm"></span>
            </div>
            <div v-else-if="informasiList.length === 0" class="text-muted small text-center py-3">
              Belum ada informasi atau agenda kampus.
            </div>
            <div
              v-for="i in informasiList.slice(0, 4)"
              :key="i.id"
              class="d-flex align-items-start gap-2 mb-3 pb-2 border-bottom info-item"
            >
              <div class="info-icon" :class="i.kategori === 'AGENDA' ? 'bg-primary-subtle text-primary' : 'bg-warning-subtle text-warning'">
                <i class="bi" :class="i.kategori === 'AGENDA' ? 'bi-calendar-event' : 'bi-megaphone'"></i>
              </div>
              <div class="flex-grow-1">
                <div class="fw-semibold small">{{ i.judul }}</div>
                <div class="text-muted small">{{ i.isi }}</div>
                <small class="text-muted">{{ formatDate(i.tanggal) }}</small>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-5">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-header bg-white fw-semibold">
            <i class="bi bi-bell text-danger me-2"></i>Perlu Perhatian Anda
          </div>
          <div class="card-body">
            <div v-if="notifLoading" class="text-center py-3">
              <span class="spinner-border spinner-border-sm"></span>
            </div>
            <div v-else-if="notifList.length === 0" class="text-muted small text-center py-3">
              Tidak ada hal yang perlu ditindaklanjuti saat ini.
            </div>
            <div v-for="(n, idx) in notifList.slice(0, 5)" :key="idx" class="d-flex align-items-start gap-2 mb-2">
              <i class="bi mt-1" :class="notifIcon(n.tingkat)"></i>
              <div>
                <div class="small fw-semibold">{{ n.judul }}</div>
                <div class="text-muted small">{{ n.pesan }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-header bg-white fw-semibold">Program Kerja Terbaru</div>
      <div class="table-responsive">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr>
              <th>Nama Program</th>
              <th>Diajukan Oleh</th>
              <th>Tempat</th>
              <th>Status</th>
              <th>Tanggal</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="6" class="text-center py-4">
                <span class="spinner-border spinner-border-sm"></span> Memuat data...
              </td>
            </tr>
            <tr v-else-if="terbaru.length === 0">
              <td colspan="6" class="text-center py-5">
                <i class="bi bi-inbox display-5 text-muted d-block mb-2"></i>
                <div class="text-muted">Belum ada program kerja</div>
                <small class="text-muted">Program kerja yang diajukan akan muncul di sini.</small>
              </td>
            </tr>
            <tr v-for="pk in terbaru" :key="pk.id">
              <td>{{ pk.namaProgram }}</td>
              <td>{{ pk.namaPengaju || pk.user?.nama }}</td>
              <td>{{ pk.namaTempat || pk.tempat?.namaTempat || '-' }}</td>
              <td>
                <span class="badge" :class="statusBadge(pk.status)">{{ pk.status }}</span>
              </td>
              <td>{{ formatDate(pk.tanggalPengajuan) }}</td>
              <td>
                <router-link :to="`/program-kerja/${pk.id}`" class="btn btn-sm btn-outline-primary">
                  Detail
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useProgramKerjaStore } from '@/stores/programKerja'
import { useAuthStore } from '@/stores/auth'
import informasiService from '@/services/informasiService'
import notifikasiService from '@/services/notifikasiService'

const store = useProgramKerjaStore()
const authStore = useAuthStore()
const statistik = ref({})
const loading = ref(true)

const informasiList = ref([])
const informasiLoading = ref(true)
const notifList = ref([])
const notifLoading = ref(true)

const terbaru = computed(() => store.list.slice(0, 5))

const notifIcon = (tingkat) => {
  const map = {
    DANGER: 'bi-exclamation-circle-fill text-danger',
    WARNING: 'bi-exclamation-triangle-fill text-warning',
    INFO: 'bi-info-circle-fill text-primary',
  }
  return map[tingkat] || 'bi-bell-fill text-secondary'
}

const bannerTitle = computed(() => {
  if (authStore.isUkm) return 'Informasi untuk UKM'
  if (authStore.isPembina) return 'Informasi untuk Pembina'
  return 'Informasi untuk Program Kerja Unit Kegiatan Mahasiswa'
})

const bannerText = computed(() => {
  if (authStore.isUkm)
    return 'Ajukan program kerja, unggah proposal, lalu pantau status persetujuan dan unggah laporan kegiatan setelah disetujui WK III.'
  if (authStore.isPembina)
    return 'Tinjau program kerja dan laporan kegiatan UKM, lalu berikan catatan dan saran sebagai bahan evaluasi.'
  return 'Sistem ini digunakan untuk mengelola, mengajukan, meninjau, dan memantau seluruh program kerja UKM secara terstruktur, transparan, dan akuntabel. Mari bersama mendukung setiap ide menjadi aksi nyata!'
})

const statusBadge = (status) => {
  const map = {
    MENUNGGU: 'bg-warning text-dark',
    REVISI: 'bg-danger',
    DISETUJUI: 'bg-success',
    SELESAI: 'bg-secondary',
  }
  return map[status] || 'bg-light text-dark'
}

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('id-ID') : '-')

onMounted(async () => {
  loading.value = true
  try {
    await Promise.all([store.fetchAll(), store.fetchStatistik()])
    statistik.value = store.statistik
  } finally {
    loading.value = false
  }

  informasiLoading.value = true
  try {
    const res = await informasiService.getAll()
    informasiList.value = res.data.data
  } catch (err) {
    // biarkan daftar kosong jika gagal memuat
  } finally {
    informasiLoading.value = false
  }

  notifLoading.value = true
  try {
    const res = await notifikasiService.getAll()
    notifList.value = res.data.data
  } catch (err) {
    // biarkan daftar kosong jika gagal memuat
  } finally {
    notifLoading.value = false
  }
})
</script>

<style scoped>
.banner-card {
  background: linear-gradient(135deg, #eaf3ff 0%, #f5f9ff 100%);
}
.banner-icon {
  width: 40px;
  height: 40px;
  min-width: 40px;
  border-radius: 10px;
  background: #0d6efd;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
}
.banner-deco {
  font-size: 2.5rem;
  color: #b6d4ff;
}
.stat-card {
  transition: transform 0.15s ease;
}
.stat-card:hover {
  transform: translateY(-2px);
}
.stat-icon {
  width: 34px;
  height: 34px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.info-icon {
  width: 34px;
  height: 34px;
  min-width: 34px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.info-item:last-child {
  border-bottom: none !important;
  margin-bottom: 0 !important;
  padding-bottom: 0 !important;
}
</style>
