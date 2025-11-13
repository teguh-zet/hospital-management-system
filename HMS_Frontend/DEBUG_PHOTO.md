# Debug Photo Tidak Tampil

## Masalah
Foto profil tidak tampil di halaman My Profile

## Kemungkinan Penyebab

### 1. URL Foto Salah
- Base URL dari axios: `http://localhost:9090/api/`
- Static files di-serve di: `http://localhost:9090/uploads/` (tanpa `/api/`)
- **Solusi:** Function `getPhotoUrl` sudah diperbaiki untuk menghapus `/api/` dari base URL

### 2. Security Config Blocking
- `/uploads/**` mungkin diblokir oleh Spring Security
- **Solusi:** Sudah ditambahkan `.antMatchers("/uploads/**").permitAll()` di SecurityConfig

### 3. Foto Belum Di-upload
- User mungkin belum upload foto
- **Cek:** Lihat di console log apakah `userStore.currentUser?.photo` ada nilainya

### 4. File Tidak Ada di Server
- File mungkin tidak tersimpan dengan benar
- **Cek:** Lihat di directory `${user.home}/hms-uploads/`

### 5. Port Backend Salah
- Backend mungkin running di port berbeda
- **Cek:** application.properties/yml untuk port yang digunakan

## Perbaikan yang Sudah Dilakukan

### 1. getPhotoUrl Function
```javascript
const getPhotoUrl = (photoPath) => {
  if (!photoPath) return ''
  if (photoPath.startsWith('/uploads/')) {
    let baseUrl = PrivateAxios.defaults.baseURL || 'http://localhost:9090/api'
    // Remove /api/ or /api suffix
    if (baseUrl.endsWith('/api/')) {
      baseUrl = baseUrl.replace('/api/', '')
    } else if (baseUrl.endsWith('/api')) {
      baseUrl = baseUrl.replace('/api', '')
    }
    return `${baseUrl}${photoPath}`
  }
  return photoPath
}
```

### 2. SecurityConfig
```java
.antMatchers("/uploads/**").permitAll() // Allow access to uploaded files
```

### 3. Debug Logging
- Console log photo path saat component mount
- Console log photo URL yang dihasilkan
- Error logging jika image gagal load

## Cara Debug

### 1. Cek Browser Console
Buka Developer Tools (F12) → Console:
- Lihat log: "User photo path: ..."
- Lihat log: "Photo URL: ..."
- Lihat error jika ada

### 2. Cek Network Tab
Buka Developer Tools (F12) → Network:
- Filter: "Img" atau "uploads"
- Cari request ke `/uploads/...`
- Lihat status code:
  - 200 = OK (file ada)
  - 404 = File tidak ditemukan
  - 403 = Access denied
  - 401 = Unauthorized

### 3. Cek Photo Path di Database
```sql
SELECT id, email, photo FROM users WHERE id = {userId};
```

### 4. Cek File di Server
**Windows:**
```
C:\Users\{username}\hms-uploads\
```

**Linux/Mac:**
```
~/hms-uploads/
```

### 5. Test URL Langsung
Buka di browser:
```
http://localhost:9090/uploads/{filename}
```

## Expected URL Format

**Photo Path di Database:**
```
/uploads/user_1_uuid.jpg
```

**Full URL yang Dihasilkan:**
```
http://localhost:9090/uploads/user_1_uuid.jpg
```

## Checklist

- [ ] Backend running di port 9090 (atau sesuai config)
- [ ] Foto sudah di-upload (cek di database)
- [ ] File ada di upload directory
- [ ] SecurityConfig mengizinkan `/uploads/**`
- [ ] WebConfig sudah dikonfigurasi dengan benar
- [ ] URL yang dihasilkan benar (cek console log)
- [ ] Network request ke foto return 200 OK
- [ ] Tidak ada CORS error

## Langkah Troubleshooting

1. **Cek apakah foto sudah di-upload:**
   - Buka My Profile
   - Lihat console log untuk photo path
   - Jika null/undefined, foto belum di-upload

2. **Cek URL yang dihasilkan:**
   - Lihat console log untuk Photo URL
   - Copy URL dan buka di browser baru
   - Jika 404, file tidak ada atau path salah

3. **Cek Network Request:**
   - Buka Network tab
   - Refresh halaman
   - Cari request ke `/uploads/...`
   - Lihat status code dan response

4. **Cek Backend Logs:**
   - Lihat console backend
   - Cek apakah ada error saat serve static files

5. **Test Upload Foto Baru:**
   - Upload foto baru
   - Cek apakah file tersimpan di upload directory
   - Cek apakah path tersimpan di database

## Solusi Cepat

Jika foto masih tidak tampil:

1. **Upload foto baru:**
   - Klik "Upload Photo"
   - Pilih gambar
   - Upload
   - Refresh halaman

2. **Cek console log:**
   - Lihat photo path dan URL
   - Pastikan URL benar

3. **Test URL manual:**
   - Copy URL dari console
   - Buka di browser baru
   - Jika bisa dibuka, masalah di frontend
   - Jika tidak bisa, masalah di backend

4. **Restart Backend:**
   - Restart backend service
   - Pastikan WebConfig dan SecurityConfig ter-load

## Expected Console Output

**Jika foto ada:**
```
User photo path: /uploads/user_1_uuid.jpg
Photo URL: http://localhost:9090/uploads/user_1_uuid.jpg
Image loaded: http://localhost:9090/uploads/user_1_uuid.jpg
```

**Jika foto tidak ada:**
```
(no logs about photo)
```

**Jika error:**
```
Error loading image: http://localhost:9090/uploads/user_1_uuid.jpg
Photo path from user: /uploads/user_1_uuid.jpg
```

