<template>
  <div class="d-flex" style="min-height: 100vh;">
    <!-- Sidebar -->
    <aside class="sidebar bg-white p-3 border-end" :class="{ 'sidebar-open': sidebarOpen }">
      <div class="d-flex align-items-center mb-4">
        <img :src="logo" alt="Logo STMIK Mardira Indonesia" class="sidebar-logo me-2" />
        <div>
          <div class="fw-bold lh-sm">Program Kerja UKM</div>
          <small class="text-muted">STMIK Mardira Indonesia</small>
        </div>
      </div>
      <nav class="nav flex-column gap-1">
        <router-link to="/dashboard" class="nav-link sidebar-link">
          <i class="bi bi-house-door me-2"></i> Dashboard
        </router-link>
        <router-link to="/program-kerja" class="nav-link sidebar-link">
          <i class="bi bi-kanban me-2"></i> Program Kerja
        </router-link>
        <router-link to="/proposal" class="nav-link sidebar-link">
          <i class="bi bi-file-earmark-text me-2"></i> Proposal
        </router-link>
        <router-link to="/laporan" class="nav-link sidebar-link">
          <i class="bi bi-bar-chart me-2"></i> Laporan
        </router-link>
        <router-link to="/catatan" class="nav-link sidebar-link">
          <i class="bi bi-chat-square-text me-2"></i> Catatan
        </router-link>
        <router-link to="/tempat" class="nav-link sidebar-link">
          <i class="bi bi-geo-alt me-2"></i> Tempat
        </router-link>
        <router-link to="/ukm" class="nav-link sidebar-link">
          <i class="bi bi-people-fill me-2"></i> Kelola Data UKM
        </router-link>
        <router-link to="/informasi" class="nav-link sidebar-link">
          <i class="bi bi-megaphone me-2"></i> Informasi & Agenda
        </router-link>
        <router-link
          v-if="authStore.isWkIII"
          to="/users"
          class="nav-link sidebar-link"
        >
          <i class="bi bi-people me-2"></i> Kelola Pengguna
        </router-link>
        <hr class="text-muted my-2" />
        <router-link to="/profil" class="nav-link sidebar-link">
          <i class="bi bi-person-circle me-2"></i> Profil
        </router-link>
        <router-link to="/pengaturan" class="nav-link sidebar-link">
          <i class="bi bi-gear me-2"></i> Pengaturan
        </router-link>
      </nav>
    </aside>

    <!-- Main content -->
    <div class="flex-grow-1 d-flex flex-column">
      <header class="navbar navbar-light bg-white border-bottom px-3 py-2">
        <button class="btn btn-outline-secondary d-md-none" @click="sidebarOpen = !sidebarOpen">
          <i class="bi bi-list"></i>
        </button>
        <span class="fw-semibold ms-2">{{ pageTitle }}</span>
        <div class="ms-auto d-flex align-items-center gap-2">
          <div class="dropdown">
            <button class="btn btn-light position-relative" data-bs-toggle="dropdown" @click="loadNotifikasi">
              <i class="bi bi-bell"></i>
              <span
                v-if="notifikasiList.length > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                style="font-size: 0.6rem;"
              >
                {{ notifikasiList.length }}
              </span>
            </button>
            <div class="dropdown-menu dropdown-menu-end notif-menu p-0">
              <div class="px-3 py-2 border-bottom fw-semibold small">Notifikasi</div>
              <div v-if="notifLoading" class="text-center py-3">
                <span class="spinner-border spinner-border-sm"></span>
              </div>
              <div v-else-if="notifikasiList.length === 0" class="text-center text-muted small py-3">
                Tidak ada notifikasi baru
              </div>
              <router-link
                v-for="(n, idx) in notifikasiList"
                :key="idx"
                :to="n.programKerjaId ? `/program-kerja/${n.programKerjaId}` : '#'"
                class="dropdown-item py-2 border-bottom notif-item"
              >
                <div class="d-flex align-items-start gap-2">
                  <i class="bi mt-1" :class="notifIcon(n.tingkat)"></i>
                  <div>
                    <div class="small fw-semibold">{{ n.judul }}</div>
                    <div class="small text-muted">{{ n.pesan }}</div>
                  </div>
                </div>
              </router-link>
            </div>
          </div>
          <div class="dropdown">
            <button class="btn btn-light d-flex align-items-center" data-bs-toggle="dropdown">
              <i class="bi bi-person-circle fs-5 me-2"></i>
              <span>{{ authStore.user?.nama }}</span>
              <span class="badge bg-primary ms-2">{{ authStore.user?.role }}</span>
            </button>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><router-link class="dropdown-item" to="/profil">Profil</router-link></li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <button class="dropdown-item text-danger" @click="handleLogout">
                  <i class="bi bi-box-arrow-right me-2"></i>Logout
                </button>
              </li>
            </ul>
          </div>
        </div>
      </header>
      <main class="p-3 flex-grow-1 bg-light">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Swal from 'sweetalert2'
import logo from '@/assets/logo-stmik.png'
import notifikasiService from '@/services/notifikasiService'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const sidebarOpen = ref(false)

const notifikasiList = ref([])
const notifLoading = ref(false)

const pageTitle = computed(() => route.meta.title || '')

const loadNotifikasi = async () => {
  notifLoading.value = true
  try {
    const res = await notifikasiService.getAll()
    notifikasiList.value = res.data.data
  } catch (err) {
    // gagal diam-diam, tidak perlu mengganggu pengguna dengan alert
  } finally {
    notifLoading.value = false
  }
}

const notifIcon = (tingkat) => {
  const map = {
    DANGER: 'bi-exclamation-circle-fill text-danger',
    WARNING: 'bi-exclamation-triangle-fill text-warning',
    INFO: 'bi-info-circle-fill text-primary',
  }
  return map[tingkat] || 'bi-bell-fill text-secondary'
}

const handleLogout = async () => {
  const result = await Swal.fire({
    title: 'Keluar dari akun?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Ya, keluar',
    cancelButtonText: 'Batal',
  })
  if (result.isConfirmed) {
    authStore.logout()
    router.push('/login')
  }
}

onMounted(loadNotifikasi)
</script>

<style scoped>
.sidebar {
  width: 240px;
  min-width: 240px;
}
.sidebar-logo {
  width: 38px;
  height: auto;
}
.sidebar-link {
  border-radius: 8px;
  padding: 8px 12px;
  color: #495057;
}
.sidebar-link:hover {
  background-color: #f1f3f5;
}
.sidebar-link.router-link-active {
  background-color: #e7f1ff;
  color: #0d6efd;
  font-weight: 600;
}
.notif-menu {
  width: 320px;
  max-width: 90vw;
  max-height: 400px;
  overflow-y: auto;
}
.notif-item {
  white-space: normal;
}
.notif-item:last-child {
  border-bottom: none !important;
}
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: -240px;
    top: 0;
    bottom: 0;
    z-index: 1000;
    transition: left 0.2s;
  }
  .sidebar.sidebar-open {
    left: 0;
  }
}
</style>
