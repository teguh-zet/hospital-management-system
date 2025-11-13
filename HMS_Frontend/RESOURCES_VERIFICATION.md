# Verifikasi Resources Management

## Status Backend Services ✅

Dari Eureka Dashboard, semua service sudah berjalan:
- ✅ API-GATEWAY (port 9090)
- ✅ HMS-SERVICE (port 9091)  
- ✅ RESOURCE-SERVICE (port 9092)

## Langkah Verifikasi Resources Management

### 1. Pastikan MongoDB Berjalan
Resources API menggunakan MongoDB. Pastikan MongoDB service berjalan:
```bash
# Windows (jika MongoDB sebagai service)
# Cek di Services atau jalankan:
mongod
```

### 2. Test API Endpoint
Test endpoint resources melalui browser atau Postman:
```
GET http://localhost:9090/api/resources?pageNumber=0&pageSize=10&sortBy=id&sortDir=asc
Headers:
  Authorization: Bearer <your-jwt-token>
```

### 3. Cek Frontend Console
1. Buka halaman Resources Management di browser
2. Tekan F12 untuk membuka DevTools
3. Buka tab Console
4. Cari log "Resources response:"
5. Cek apakah ada error

### 4. Cek Network Tab
1. Buka tab Network di DevTools
2. Refresh halaman Resources Management
3. Filter untuk "resources"
4. Cek request:
   - URL: `http://localhost:9090/api/resources?...`
   - Method: GET
   - Status: 200 OK
5. Cek response:
   - Struktur harus seperti:
   ```json
   {
     "content": [...],
     "pageNumber": 0,
     "pageSize": 10,
     "totalElements": 0,
     "totalPages": 0,
     "lastPage": true
   }
   ```

## Troubleshooting

### Jika Resources List Kosong
1. **Cek apakah ada data di database**
   - Buka MongoDB Compass atau mongo shell
   - Connect ke `mongodb://localhost:27017`
   - Database: `hospital_management_resource_service`
   - Collection: `resources`
   - Cek apakah ada documents

2. **Jika database kosong, tambah resource dulu**
   - Buka halaman "Add Resources"
   - Isi form dan submit
   - Cek apakah berhasil

### Jika Error 403 Forbidden
- Pastikan user yang login memiliki role ADMIN
- Cek apakah token JWT valid
- Cek apakah endpoint memerlukan authentication

### Jika Error 500 atau Connection Error
- Pastikan RESOURCE-SERVICE benar-benar berjalan
- Cek logs RESOURCE-SERVICE
- Pastikan MongoDB connection berhasil
- Cek apakah API Gateway bisa route ke RESOURCE-SERVICE

## Expected Behavior

Setelah semua service berjalan:
1. Halaman Resources Management harus menampilkan loading spinner
2. Setelah data loaded, harus menampilkan:
   - Tabel dengan kolom: ID, Name, Type, Quantity, Price, Description, Actions
   - Pagination jika totalPages > 1
   - Button "Delete" untuk setiap resource
3. Jika tidak ada data, menampilkan pesan "No resources found" dengan button "Add First Resource"

## Catatan

Warning di Eureka tentang "RENEWALS ARE LESSER THAN THRESHOLD" biasanya tidak kritis jika semua service tetap UP. Ini hanya peringatan bahwa renewals rate sedikit rendah, tapi service masih berfungsi normal.

