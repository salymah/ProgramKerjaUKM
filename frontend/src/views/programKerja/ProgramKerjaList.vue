<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h4 class="fw-bold mb-0">Program Kerja</h4>
      <router-link v-if="authStore.isUkm" to="/program-kerja/ajukan" class="btn btn-primary">
        <i class="bi bi-plus-lg me-1"></i> Ajukan Program Kerja
      </router-link>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="table-responsive">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr>
              <th>Nama Program</th>
              <th>Diajukan Oleh</th>
              <th>Tempat</th>
              <th>Tanggal Pengajuan</th>
              <th>Tanggal Pelaksanaan</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="store.loading">
              <td colspan="7" class="text-center py-4">
                <span class="spinner-border spinner-border-sm"></span> Memuat data...
              </td>
            </tr>
            <tr v-else-if="store.list.length === 0">
              <td colspan="7" class="text-center text-muted py-4">Belum ada program kerja</td>
            </tr>
            <tr v-for="pk in store.list" :key="pk.id">
              <td>{{ pk.namaProgram }}</td>
              <td>{{ pk.namaPengaju || pk.user?.nama }}</td>
              <td>{{ pk.namaTempat || pk.tempat?.namaTempat || '-' }}</td>
              <td>{{ formatDate(pk.tanggalPengajuan) }}</td>
              <td>{{ formatDate(pk.tanggalKegiatan) }}</td>
              <td><span class="badge" :class="statusBadge(pk.status)">{{ pk.status }}</span></td>
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
import { onMounted } from 'vue'
import { useProgramKerjaStore } from '@/stores/programKerja'
import { useAuthStore } from '@/stores/auth'

const store = useProgramKerjaStore()
const authStore = useAuthStore()

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

onMounted(() => store.fetchAll())
</script>
