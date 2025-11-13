# Debug Guide - Error 404

## Langkah-langkah Debugging:

### 1. **Buka Browser Console (F12)**
   - Tekan F12 di browser
   - Buka tab **Console**
   - Refresh halaman (F5)
   - **Catat semua error yang muncul** (warna merah)

### 2. **Cek Network Tab**
   - Di Developer Tools, buka tab **Network**
   - Refresh halaman (F5)
   - Filter: **Failed** atau **All**
   - Cari request ke:
     - `/src/main.js` - harus status 200
     - `/src/App.vue` - harus status 200
     - Komponen lainnya

### 3. **Test Halaman Sederhana**
   Coba akses: `http://localhost:3000/test`
   - Jika halaman test muncul → Vue.js bekerja, masalah di komponen Home
   - Jika masih 404 → Masalah di routing atau server

### 4. **Cek Terminal**
   Lihat terminal yang menjalankan `npm run dev`:
   - Apakah ada error?
   - Apakah server benar-benar running?
   - Port berapa yang digunakan?

### 5. **Restart Server**
   ```bash
   # Stop server (Ctrl + C)
   # Hapus cache
   rm -rf node_modules/.vite
   # Atau di Windows:
   Remove-Item -Recurse -Force node_modules\.vite
   
   # Jalankan lagi
   npm run dev
   ```

### 6. **Cek File Structure**
   Pastikan file-file ini ada:
   - ✅ `public/index.html`
   - ✅ `src/main.js`
   - ✅ `src/App.vue`
   - ✅ `src/router/index.js`
   - ✅ `src/components/Home/Home.vue`

### 7. **Clear Browser Cache**
   - Chrome/Edge: `Ctrl + Shift + Delete` → Clear cache
   - Atau: Incognito/Private window

### 8. **Cek Error di Console**
   Error umum:
   - `Failed to fetch dynamically imported module` → File tidak ditemukan
   - `Cannot find module` → Import path salah
   - `Unexpected token` → Syntax error
   - `404 (Not Found)` → File tidak ada di server

## Informasi yang Diperlukan:

Jika masih error, kirimkan:
1. **Screenshot Console** (tab Console di F12)
2. **Screenshot Network** (tab Network, filter: Failed)
3. **Output terminal** saat menjalankan `npm run dev`
4. **URL yang diakses** (misalnya: `http://localhost:3000/`)


