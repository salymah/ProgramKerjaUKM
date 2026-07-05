import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/dashboard' },
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: 'Dashboard' },
      },
      {
        path: 'program-kerja',
        name: 'program-kerja',
        component: () => import('@/views/programKerja/ProgramKerjaList.vue'),
        meta: { title: 'Program Kerja' },
      },
      {
        path: 'program-kerja/ajukan',
        name: 'program-kerja-ajukan',
        component: () => import('@/views/programKerja/ProgramKerjaForm.vue'),
        meta: { title: 'Ajukan Program Kerja', roles: ['UKM'] },
      },
      {
        path: 'program-kerja/:id',
        name: 'program-kerja-detail',
        component: () => import('@/views/programKerja/ProgramKerjaDetail.vue'),
        meta: { title: 'Detail Program Kerja' },
      },
      {
        path: 'proposal',
        name: 'proposal',
        component: () => import('@/views/ProposalList.vue'),
        meta: { title: 'Proposal' },
      },
      {
        path: 'laporan',
        name: 'laporan',
        component: () => import('@/views/LaporanList.vue'),
        meta: { title: 'Laporan' },
      },
      {
        path: 'catatan',
        name: 'catatan',
        component: () => import('@/views/CatatanList.vue'),
        meta: { title: 'Catatan' },
      },
      {
        path: 'tempat',
        name: 'tempat',
        component: () => import('@/views/TempatList.vue'),
        meta: { title: 'Tempat' },
      },
      {
        path: 'ukm',
        name: 'ukm',
        component: () => import('@/views/UkmList.vue'),
        meta: { title: 'Kelola Data UKM' },
      },
      {
        path: 'informasi',
        name: 'informasi',
        component: () => import('@/views/Informasi.vue'),
        meta: { title: 'Informasi & Agenda Kampus' },
      },
      {
        path: 'users',
        name: 'users',
        component: () => import('@/views/UserList.vue'),
        meta: { title: 'Kelola Pengguna', roles: ['WKIII'] },
      },
      {
        path: 'profil',
        name: 'profil',
        component: () => import('@/views/Profil.vue'),
        meta: { title: 'Profil' },
      },
      {
        path: 'pengaturan',
        name: 'pengaturan',
        component: () => import('@/views/Pengaturan.vue'),
        meta: { title: 'Pengaturan' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/NotFound.vue'),
    meta: { public: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.public) {
    // Kalau sudah login dan coba akses /login, lempar ke dashboard
    if (to.name === 'login' && authStore.isLoggedIn) {
      return next('/dashboard')
    }
    return next()
  }

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    return next('/login')
  }

  if (to.meta.roles && !to.meta.roles.includes(authStore.role)) {
    // Role tidak diizinkan akses halaman ini
    return next('/dashboard')
  }

  next()
})

export default router
