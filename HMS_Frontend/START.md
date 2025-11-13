# Cara Menjalankan Frontend

## Langkah-langkah:

### 1. Pastikan Node.js sudah terinstall
```bash
node --version
npm --version
```

### 2. Install Dependencies (jika belum)
```bash
cd HMS_Frontend
npm install
```

### 3. Jalankan Development Server
```bash
npm run dev
```

### 4. Buka Browser
Setelah server berjalan, buka browser dan akses:
- **URL**: `http://localhost:3000`
- Atau URL yang ditampilkan di terminal (biasanya `http://localhost:3000`)

## Troubleshooting

### Error 404 di localhost:3000
**Penyebab**: Server development belum berjalan

**Solusi**:
1. Pastikan Anda sudah menjalankan `npm run dev` di terminal
2. Tunggu sampai muncul pesan seperti:
   ```
   VITE v5.x.x  ready in xxx ms
   
   ➜  Local:   http://localhost:3000/
   ➜  Network: use --host to expose
   ```
3. Jika port 3000 sudah digunakan, Vite akan otomatis menggunakan port lain (misalnya 3001, 3002, dll)
4. Periksa terminal untuk melihat port yang digunakan

### Port sudah digunakan
Jika port 3000 sudah digunakan:
```bash
# Hentikan proses yang menggunakan port 3000, atau
# Vite akan otomatis menggunakan port lain
```

### Dependencies belum terinstall
Jika ada error tentang module tidak ditemukan:
```bash
npm install
```

### Error saat import module
Pastikan semua file sudah ada:
- `src/main.js`
- `src/App.vue`
- `src/router/index.js`
- `src/stores/user.js`
- dll

## Catatan Penting

1. **Server harus berjalan** - Aplikasi Vue.js memerlukan development server untuk berjalan
2. **Jangan tutup terminal** - Terminal yang menjalankan `npm run dev` harus tetap terbuka
3. **Hot Reload** - Perubahan file akan otomatis di-reload di browser
4. **Backend harus running** - Pastikan backend API sudah berjalan di `http://localhost:9090`


