<template>
  <div>
    <h4 class="fw-bold mb-3">Catatan & Saran</h4>
    <p class="text-muted">Pilih program kerja untuk melihat atau menambah catatan.</p>
    <div class="card border-0 shadow-sm">
      <div class="table-responsive">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr>
              <th>Nama Program</th>
              <th>Diajukan Oleh</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="store.loading">
              <td colspan="4" class="text-center py-4">
                <span class="spinner-border spinner-border-sm"></span>
              </td>
            </tr>
            <tr v-else-if="store.list.length === 0">
              <td colspan="4" class="text-center text-muted py-4">Belum ada program kerja</td>
            </tr>
            <tr v-for="pk in store.list" :key="pk.id">
              <td>{{ pk.namaProgram }}</td>
              <td>{{ pk.namaPengaju || pk.user?.nama }}</td>
              <td><span class="badge" :class="statusBadge(pk.status)">{{ pk.status }}</span></td>
              <td>
                <router-link :to="`/program-kerja/${pk.id}`" class="btn btn-sm btn-outline-primary">
                  Lihat Catatan
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

const store = useProgramKerjaStore()

const statusBadge = (status) => {
  const map = {
    MENUNGGU: 'bg-warning text-dark',
    REVISI: 'bg-danger',
    DISETUJUI: 'bg-success',
    SELESAI: 'bg-secondary',
  }
  return map[status] || 'bg-light text-dark'
}

onMounted(() => store.fetchAll())
</script>
