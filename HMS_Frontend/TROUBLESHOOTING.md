# Troubleshooting Error 404

## Jika Server Sudah Running Tapi Masih 404

### 1. **Hard Refresh Browser**
Browser mungkin menggunakan cache lama. Lakukan:
- **Windows/Linux**: `Ctrl + Shift + R` atau `Ctrl + F5`
- **Mac**: `Cmd + Shift + R`

### 2. **Cek Console Browser**
Buka Developer Tools (F12) dan cek tab **Console**:
- Jika ada error merah, catat pesan errornya
- Error umum:
  - `Cannot find module` → Dependencies belum terinstall
  - `Failed to load resource` → File tidak ditemukan
  - `Uncaught SyntaxError` → Ada syntax error di kode

### 3. **Cek Network Tab**
Di Developer Tools, buka tab **Network**:
- Refresh halaman (F5)
- Cek apakah ada request yang gagal (status merah)
- Periksa request ke `/src/main.js` - harus status 200

### 4. **Restart Development Server**
```bash
# Di terminal, tekan Ctrl + C untuk stop server
# Kemudian jalankan lagi:
npm run dev
```

### 5. **Clear Browser Cache**
- Buka Developer Tools (F12)
- Klik kanan pada tombol refresh
- Pilih "Empty Cache and Hard Reload"

### 6. **Cek Apakah Port Benar**
Pastikan Anda mengakses URL yang ditampilkan di terminal:
- Terminal menampilkan: `http://localhost:3000/`
- Pastikan URL di browser sama persis

### 7. **Cek File index.html**
Pastikan file `public/index.html` ada dan berisi:
```html
<div id="app"></div>
<script type="module" src="/src/main.js"></script>
```

### 8. **Cek Terminal untuk Error**
Lihat terminal yang menjalankan `npm run dev`:
- Jika ada error, akan ditampilkan di terminal
- Error umum:
  - `Cannot find module` → Jalankan `npm install`
  - `Port already in use` → Port 3000 sudah digunakan

### 9. **Coba Browser Lain**
Kadang masalah spesifik browser:
- Coba Chrome, Firefox, atau Edge
- Pastikan browser sudah update

### 10. **Cek File Structure**
Pastikan struktur folder benar:
```
HMS_Frontend/
├── public/
│   └── index.html
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── router/
│   └── components/
├── package.json
└── vite.config.js
```

## Langkah Debugging

1. **Buka Console Browser (F12)**
2. **Refresh halaman**
3. **Catat semua error yang muncul**
4. **Cek apakah `main.js` ter-load**:
   - Di Network tab, cari `main.js`
   - Harus status 200 (OK)

## Jika Masih Error

Kirimkan informasi berikut:
1. Screenshot error di Console browser
2. Screenshot Network tab (filter: Failed)
3. Output terminal saat menjalankan `npm run dev`
4. Versi Node.js: `node --version`
5. Versi npm: `npm --version`


