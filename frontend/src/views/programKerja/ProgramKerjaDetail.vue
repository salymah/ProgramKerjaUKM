<template>
  <div v-if="loading" class="text-center py-5">
    <span class="spinner-border"></span>
  </div>
  <div v-else-if="pk">
    <div class="d-flex justify-content-between align-items-start mb-3">
      <div>
        <h4 class="fw-bold mb-1">{{ pk.namaProgram }}</h4>
        <span class="badge" :class="statusBadge(pk.status)">{{ pk.status }}</span>
      </div>
      <router-link to="/program-kerja" class="btn btn-light btn-sm no-print">
        <i class="bi bi-arrow-left me-1"></i> Kembali
      </router-link>
    </div>

    <div class="row g-3">
      <div class="col-md-8">
        <div class="card border-0 shadow-sm mb-3 no-print">
          <div class="card-body">
            <h6 class="fw-semibold">Deskripsi</h6>
            <p class="text-muted mb-3">{{ pk.deskripsi }}</p>
            <div class="row">
              <div class="col-6">
                <small class="text-muted">Diajukan Oleh</small>
                <div>{{ pk.namaPengaju || pk.user?.nama }}</div>
              </div>
              <div class="col-6">
                <small class="text-muted">Tempat</small>
                <div>{{ pk.namaTempat || pk.tempat?.namaTempat || '-' }}</div>
              </div>
              <div class="col-6 mt-2">
                <small class="text-muted">Tanggal Pengajuan</small>
                <div>{{ formatDate(pk.tanggalPengajuan) }}</div>
              </div>
              <div class="col-6 mt-2">
                <small class="text-muted">Tanggal Pelaksanaan</small>
                <div>{{ formatDate(pk.tanggalKegiatan) }}</div>
              </div>
              <div class="col-6 mt-2">
                <small class="text-muted">Tanggal Verifikasi</small>
                <div>{{ formatDate(pk.tanggalVerifikasi) }}</div>
              </div>
              <div class="col-12 mt-2" v-if="pk.keterangan">
                <small class="text-muted">Keterangan</small>
                <div>{{ pk.keterangan }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Verifikasi WK III -->
        <div class="card border-0 shadow-sm mb-3 no-print" v-if="authStore.isWkIII && pk.status === 'MENUNGGU'">
          <div class="card-body">
            <h6 class="fw-semibold mb-3">Verifikasi Program Kerja</h6>
            <div class="mb-2">
              <textarea v-model="keterangan" class="form-control" rows="2" placeholder="Keterangan (opsional)"></textarea>
            </div>
            <button class="btn btn-success btn-sm me-2" @click="handleVerifikasi('DISETUJUI')">
              <i class="bi bi-check-lg me-1"></i> Setujui
            </button>
            <button class="btn btn-outline-danger btn-sm" @click="handleVerifikasi('REVISI')">
              <i class="bi bi-arrow-counterclockwise me-1"></i> Minta Revisi
            </button>
          </div>
        </div>

        <!-- Proposal -->
        <div class="card border-0 shadow-sm mb-3 no-print">
          <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
            <span>Proposal</span>
            <label v-if="authStore.isUkm" class="btn btn-sm btn-primary mb-0">
              <i class="bi bi-upload me-1"></i> Upload
              <input type="file" hidden @change="(e) => handleUpload('proposal', e)" />
            </label>
          </div>
          <ul class="list-group list-group-flush">
            <li v-if="proposalList.length === 0" class="list-group-item text-muted">
              Belum ada proposal diunggah
            </li>
            <li v-for="p in proposalList" :key="p.id" class="list-group-item d-flex justify-content-between align-items-center">
              <span><i class="bi bi-file-earmark-pdf me-2"></i>{{ p.namaFile }}</span>
              <button class="btn btn-sm btn-outline-secondary" @click="handleDownload('proposal', p)">
                <i class="bi bi-download"></i>
              </button>
            </li>
          </ul>
        </div>

        <!-- Laporan -->
        <div class="card border-0 shadow-sm mb-3 print-area" v-if="pk.status === 'DISETUJUI' || pk.status === 'SELESAI'">
          <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
            <span>Laporan Kegiatan</span>
            <div class="d-flex gap-2">
              <label v-if="authStore.isUkm" class="btn btn-sm btn-primary mb-0">
                <i class="bi bi-upload me-1"></i> Upload
                <input type="file" hidden @change="(e) => handleUpload('laporan', e)" />
              </label>
              <button
                v-if="(authStore.isWkIII || authStore.isPembina) && laporanList.length > 0"
                class="btn btn-sm btn-outline-secondary no-print"
                @click="handlePrint"
              >
                <i class="bi bi-printer me-1"></i> Cetak Laporan
              </button>
            </div>
          </div>
          <ul class="list-group list-group-flush">
            <li v-if="laporanList.length === 0" class="list-group-item text-muted">
              Belum ada laporan diunggah
            </li>
            <li v-for="l in laporanList" :key="l.id" class="list-group-item d-flex justify-content-between align-items-center">
              <span><i class="bi bi-file-earmark-check me-2"></i>{{ l.namaFile }}</span>
              <button class="btn btn-sm btn-outline-secondary no-print" @click="handleDownload('laporan', l)">
                <i class="bi bi-download"></i>
              </button>
            </li>
          </ul>
        </div>
      </div>

      <div class="col-md-4 no-print">
        <!-- Catatan -->
        <div class="card border-0 shadow-sm">
          <div class="card-header bg-white fw-semibold">Catatan & Saran</div>
          <div class="card-body" style="max-height: 400px; overflow-y: auto;">
            <div v-if="catatanList.length === 0" class="text-muted small">Belum ada catatan</div>
            <div v-for="c in catatanList" :key="c.id" class="mb-3 pb-2 border-bottom">
              <div class="d-flex justify-content-between">
                <strong class="small">{{ c.namaPenulis || c.user?.nama }}</strong>
                <small class="text-muted">{{ formatDate(c.tanggal) }}</small>
              </div>
              <div class="small">{{ c.isiCatatan }}</div>
            </div>
          </div>
          <div class="card-footer bg-white" v-if="authStore.isWkIII || authStore.isPembina">
            <textarea v-model="catatanBaru" class="form-control mb-2" rows="2" placeholder="Tulis catatan..."></textarea>
            <button class="btn btn-sm btn-primary w-100" @click="handleAddCatatan">Kirim Catatan</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Swal from 'sweetalert2'
import { useAuthStore } from '@/stores/auth'
import programKerjaService from '@/services/programKerjaService'
import proposalService from '@/services/proposalService'
import laporanService from '@/services/laporanService'
import catatanService from '@/services/catatanService'

const route = useRoute()
const authStore = useAuthStore()
const id = route.params.id

const pk = ref(null)
const loading = ref(true)
const keterangan = ref('')
const catatanBaru = ref('')

const proposalList = ref([])
const laporanList = ref([])
const catatanList = ref([])

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

const loadAll = async () => {
  loading.value = true
  try {
    const [pkRes, propRes, lapRes, catRes] = await Promise.all([
      programKerjaService.getById(id),
      proposalService.getByProgramKerja(id),
      laporanService.getByProgramKerja(id),
      catatanService.getByProgramKerja(id),
    ])
    pk.value = pkRes.data.data
    proposalList.value = propRes.data.data
    laporanList.value = lapRes.data.data
    catatanList.value = catRes.data.data
  } catch (err) {
    Swal.fire('Gagal', 'Data program kerja tidak ditemukan', 'error')
  } finally {
    loading.value = false
  }
}

const handleVerifikasi = async (status) => {
  try {
    await programKerjaService.verifikasi(id, { status, keterangan: keterangan.value })
    await Swal.fire('Berhasil', `Program kerja telah ${status.toLowerCase()}`, 'success')
    loadAll()
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal memverifikasi', 'error')
  }
}

const handleUpload = async (jenis, event) => {
  const file = event.target.files[0]
  if (!file) return
  try {
    if (jenis === 'proposal') {
      await proposalService.upload(id, file)
    } else {
      await laporanService.upload(id, file)
    }
    await Swal.fire('Berhasil', 'File berhasil diunggah', 'success')
    loadAll()
  } catch (err) {
    Swal.fire('Gagal', err.response?.data?.message || 'Gagal mengunggah file', 'error')
  }
}

const handlePrint = () => {
  window.print()
}

const handleDownload = async (jenis, item) => {
  try {
    const res =
      jenis === 'proposal'
        ? await proposalService.download(item.id)
        : await laporanService.download(item.id)
    const url = window.URL.createObjectURL(new Blob([res.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', item.namaFile)
    document.body.appendChild(link)
    link.click()
    link.remove()
  } catch (err) {
    Swal.fire('Gagal', 'Gagal mengunduh file', 'error')
  }
}

const handleAddCatatan = async () => {
  if (!catatanBaru.value.trim()) return
  try {
    await catatanService.create(id, { isiCatatan: catatanBaru.value })
    catatanBaru.value = ''
    loadAll()
  } catch (err) {
    Swal.fire('Gagal', 'Gagal menambah catatan', 'error')
  }
}

onMounted(loadAll)
</script>
