<template>
  <div>
    <h4 class="fw-bold mb-3">Profil Saya</h4>
    <div class="card border-0 shadow-sm" style="max-width: 500px;">
      <div class="card-body">
        <div class="text-center mb-3">
          <i class="bi bi-person-circle display-3 text-primary"></i>
        </div>
        <table class="table table-borderless mb-0">
          <tbody>
            <tr>
              <td class="text-muted" style="width: 140px;">Nama</td>
              <td class="fw-semibold">{{ authStore.user?.nama }}</td>
            </tr>
            <tr>
              <td class="text-muted">Username</td>
              <td class="fw-semibold">{{ authStore.user?.username }}</td>
            </tr>
            <tr>
              <td class="text-muted">Role</td>
              <td><span class="badge bg-secondary">{{ authStore.user?.role }}</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()
</script>
