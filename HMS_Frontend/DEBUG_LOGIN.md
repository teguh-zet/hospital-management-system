# Debug Login Error

## Error yang Terjadi
AxiosError di Login.vue:154 saat proses login

## Kemungkinan Penyebab

### 1. Backend Tidak Berjalan
- Pastikan backend service (HMS-SERVICE) berjalan di port 9090
- Cek di browser: `http://localhost:9090/api/v1/auth/login` (harus return error jika tidak ada body, bukan 404)

### 2. CORS Error
- Pastikan CORS sudah dikonfigurasi di backend
- Cek browser console untuk error CORS

### 3. Network Error
- Pastikan frontend dan backend bisa saling terhubung
- Cek firewall atau antivirus yang memblokir koneksi

### 4. Response Structure Error
- Backend mengembalikan response yang tidak sesuai ekspektasi
- Roles mungkin null atau undefined

### 5. Authentication Error
- Email atau password salah
- User tidak memiliki role

## Error Handling yang Sudah Diperbaiki

### Login.vue
- ✅ Validasi response data sebelum digunakan
- ✅ Check roles tidak null/undefined
- ✅ Error handling untuk berbagai status code (400, 401, 403, 404, 500)
- ✅ Network error handling (tidak ada response)
- ✅ Request setup error handling
- ✅ Console log yang lebih detail untuk debugging

## Cara Debug

### 1. Cek Browser Console
Buka Developer Tools (F12) → Console tab, lihat error message lengkap:
- Error message
- Error response
- Status code
- Response data

### 2. Cek Network Tab
Buka Developer Tools (F12) → Network tab:
- Cari request ke `/api/v1/auth/login`
- Cek status code
- Cek response body
- Cek request payload

### 3. Cek Backend Logs
Lihat console backend untuk:
- Error stack trace
- Request yang diterima
- Response yang dikirim

### 4. Test Endpoint Manual
Gunakan Postman atau curl untuk test endpoint:
```bash
curl -X POST http://localhost:9090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"email@example.com","password":"password"}'
```

## Error Messages yang Mungkin Muncul

### Frontend
- "Tidak dapat terhubung ke server. Pastikan backend berjalan di http://localhost:9090"
  → Backend tidak running atau tidak bisa diakses
  
- "Endpoint tidak ditemukan. Pastikan backend berjalan."
  → URL endpoint salah atau backend tidak running
  
- "Email atau password salah"
  → Credentials tidak valid
  
- "User tidak memiliki role. Silakan hubungi admin."
  → User tidak punya role di database
  
- "Response tidak valid dari server"
  → Response structure tidak sesuai ekspektasi

### Backend
- "Invalid username or password !.."
  → Credentials salah
  
- 401 Unauthorized
  → Authentication failed
  
- 500 Internal Server Error
  → Server error (cek logs)

## Solusi

### 1. Pastikan Backend Running
```bash
cd HMS_Backend/HMS_Backend/HMS_apis
mvn spring-boot:run
# atau
java -jar target/HMS_apis-*.jar
```

### 2. Cek Port
- Backend: `http://localhost:9090` (cek application.properties/yml)
- Frontend: `http://localhost:3000` (cek vite.config.js)

### 3. Cek BASE_URL
File: `HMS_Frontend/src/utils/axios.js`
```javascript
export const BASE_URL = 'http://localhost:9090/api/'
```
Pastikan sesuai dengan port backend.

### 4. Cek CORS
Pastikan backend mengizinkan request dari frontend origin.

### 5. Test dengan Postman
Test endpoint login dengan Postman untuk memastikan backend berfungsi:
```
POST http://localhost:9090/api/v1/auth/login
Content-Type: application/json

{
  "username": "email@example.com",
  "password": "password123"
}
```

## Checklist

- [ ] Backend service running di port 9090
- [ ] Frontend bisa akses `http://localhost:9090`
- [ ] BASE_URL di axios.js benar
- [ ] Email dan password yang digunakan valid
- [ ] User memiliki role di database
- [ ] CORS sudah dikonfigurasi
- [ ] Tidak ada firewall/antivirus yang memblokir
- [ ] Browser console tidak ada error lain

## Next Steps

1. Buka browser console (F12)
2. Coba login lagi
3. Lihat error message yang muncul
4. Cek Network tab untuk request/response
5. Share error details untuk debugging lebih lanjut

