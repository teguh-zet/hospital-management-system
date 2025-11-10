# Hospital Management System - Frontend (Vue.js)

Aplikasi Hospital Management System yang telah dikonversi dari React ke Vue.js.

## Teknologi yang Digunakan

- **Vue.js 3** - Framework JavaScript
- **Vue Router** - Routing untuk aplikasi
- **Pinia** - State management
- **Vite** - Build tool dan development server
- **Bootstrap 5** - CSS framework
- **Axios** - HTTP client
- **Vue Toastification** - Notifikasi toast

## Instalasi

1. Install dependencies:
```bash
npm install
```

2. Jalankan development server:
```bash
npm run dev
```

3. Build untuk production:
```bash
npm run build
```

4. Preview build production:
```bash
npm run preview
```

## Struktur Proyek

```
src/
├── components/          # Komponen Vue
│   ├── Base/           # Komponen base/layout
│   ├── Pages/          # Halaman-halaman aplikasi
│   ├── Navbar/         # Navigation bar
│   ├── Footer/         # Footer
│   └── ...
├── router/             # Konfigurasi Vue Router
├── stores/             # Pinia stores
├── services/           # API services
├── utils/              # Utility functions
└── main.js            # Entry point aplikasi
```

## Status Konversi

### ✅ Sudah Dikonversi:
- Struktur dasar aplikasi (router, store, utils)
- Komponen authentication (Login, Signup, ForgotPass)
- Komponen public (Home, About, Services, Contact)
- Base components (Base, BaseCart, BaseCa, Navbar, Footer)
- Routing dan authentication guards

### ⚠️ Perlu Dikonversi (Placeholder sudah dibuat):
- Dashboard komponen (Patient, Admin, Doctor, Receptionist, Accountant)
- Komponen halaman detail (Appointment, HealthHistory, dll)
- Komponen manajemen (AddEmployee, RemoveEmployee, dll)
- Komponen resources (GetResources, AddResources, dll)

## Catatan Penting

1. **Backend API**: Pastikan backend API berjalan di `http://localhost:9090/api/`
2. **Komponen Dashboard**: Komponen dashboard saat ini masih placeholder. Silakan konversi dari komponen React yang ada di folder `src/Component/Pages/`
3. **Services**: File services sudah dibuat di `src/services/`. Pastikan semua API calls sudah dikonversi dengan benar.
4. **State Management**: Menggunakan Pinia untuk state management. Store user sudah dibuat di `src/stores/user.js`

## Konversi Komponen React ke Vue

Untuk mengkonversi komponen React ke Vue:

1. Ganti `import React` dengan setup Vue Composition API
2. Ganti `useState` dengan `ref()` atau `reactive()`
3. Ganti `useEffect` dengan `onMounted()`, `watch()`, dll
4. Ganti JSX dengan template Vue
5. Ganti `props` dengan `defineProps()`
6. Ganti event handlers dengan `@click`, `@submit`, dll
7. Ganti `useContext` dengan Pinia store

## Troubleshooting

Jika ada masalah:
1. Pastikan semua dependencies sudah terinstall
2. Pastikan backend API berjalan
3. Periksa console browser untuk error
4. Pastikan port 3000 tidak digunakan aplikasi lain
