# Masalah: Gagal Memuat Data Dokter

## Penyebab

Endpoint `/api/doctors` di backend hanya bisa diakses oleh role **RECEPTIONIST**:
```java
@PreAuthorize("hasRole('RECEPTIONIST')")
@GetMapping("/doctors")
```

Tetapi komponen berikut juga mencoba mengakses endpoint ini:
- `Appointment.vue` (Patient) - untuk memilih dokter saat booking appointment
- `AppointmentList.vue` (Receptionist) - untuk assign doctor ke patient

## Solusi

### Opsi 1: Ubah Permission di Backend (Recommended)
Ubah `DoctorController.java` untuk mengizinkan akses dari role PATIENT juga:

```java
@PreAuthorize("hasRole('RECEPTIONIST') OR hasRole('PATIENT')")
@GetMapping("/doctors")
public ResponseEntity<List<DoctorDto>> getDoctor() {
    List<DoctorDto> doctor = this.doctorService.getDoctor();
    return ResponseEntity.ok(doctor);
}
```

### Opsi 2: Buat Endpoint Khusus untuk Patient
Buat endpoint baru di backend yang bisa diakses oleh PATIENT:

```java
@PreAuthorize("hasRole('PATIENT')")
@GetMapping("/doctors/public")
public ResponseEntity<List<DoctorDto>> getDoctorsForPatient() {
    List<DoctorDto> doctor = this.doctorService.getDoctor();
    return ResponseEntity.ok(doctor);
}
```

Kemudian update service di frontend:
```javascript
// Untuk Patient
export const getAllDoctorsForPatient = () => {
    return PrivateAxios.get('/doctors/public').then((response) => response.data)
}
```

### Opsi 3: Gunakan Public Endpoint (Jika Ada)
Jika ada endpoint public yang tidak memerlukan authentication, gunakan endpoint tersebut.

## Error Handling yang Sudah Ditambahkan

Error handling sudah diperbaiki untuk memberikan pesan error yang lebih jelas:
- 403 Forbidden: "Anda tidak memiliki izin untuk mengakses daftar dokter"
- 401 Unauthorized: "Session expired. Silakan login kembali"
- Error lainnya: Menampilkan pesan error dari server

## Testing

Setelah memperbaiki backend, test dengan:
1. Login sebagai Patient
2. Buka halaman Appointment
3. Cek apakah daftar dokter muncul
4. Cek console browser untuk error

