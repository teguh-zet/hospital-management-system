# Analisis Lengkap: Error "Cannot invoke 'java.util.Set.add(Object)' because 'this.roles' is null"

## Root Cause Analysis

### Masalah Utama
Error terjadi karena **ModelMapper meng-override inisialisasi `roles`** ketika melakukan mapping dari `UserDto` ke `User` entity.

### Alur Masalah

1. **User Entity (User.java:93):**
   ```java
   private Set<Role> roles = new HashSet<>(); // Inisialisasi default
   ```

2. **UserDto (UserDto.java:50):**
   ```java
   @JsonInclude(value = Include.NON_NULL)
   private Set<Role> roles; // Bisa null (tidak dikirim dari frontend)
   ```

3. **ModelMapper Mapping:**
   ```java
   User user = this.modelMapper.map(userDto, User.class);
   ```
   - ModelMapper melihat `UserDto.roles = null`
   - ModelMapper **meng-override** inisialisasi `new HashSet<>()` di User entity
   - Hasil: `User.roles = null`

4. **Error saat addRole:**
   ```java
   user.addRole(role); // ERROR: NullPointerException karena roles masih null!
   ```

## Tempat yang Diperbaiki

### 1. EmployeeServiceImpl.java
- **Method:** `createEmployee()` (Line 88-101)
- **Method:** `createAdmin()` (Line 57-70)
- **Fix:** Tambahkan inisialisasi roles setelah mapping

### 2. PatientServiceImpl.java
- **Method:** `createPatient()` (Line 70-76)
- **Fix:** Tambahkan inisialisasi roles setelah mapping

### 3. DoctorServiceImpl.java
- **Method:** `createDoctorN()` (Line 60-66)
- **Fix:** Tambahkan inisialisasi roles setelah mapping

## Solusi yang Diterapkan

### Pattern Fix
```java
User user = this.modelMapper.map(userDto, User.class);

// Fix: Initialize roles if null (ModelMapper might set it to null)
if (user.getRoles() == null) {
    user.setRoles(new HashSet<>());
}

// ... rest of code
user.addRole(role);
```

### Import yang Ditambahkan
```java
import java.util.HashSet;
```

## Analisis Frontend

### Struktur Data yang Dikirim
```javascript
{
  user: {
    firstName: "...",
    lastName: "...",
    email: "...",
    password: "...",
    gender: "...",
    mobileNo: "...",
    securityQue: "...",
    securityAns: "...",
    address: {
      street: "",
      city: "",
      state: "",
      zipCode: "",
      country: ""
    }
    // roles TIDAK dikirim (benar, karena ditentukan oleh backend)
  },
  doctor: { ... } // Hanya jika role DOCTOR
}
```

### Field yang Wajib (Sudah Diperbaiki)
- ✅ firstName
- ✅ lastName
- ✅ email
- ✅ password
- ✅ gender (ditambahkan)
- ✅ mobileNo (ditambahkan)
- ✅ securityQue (ditambahkan)
- ✅ securityAns (ditambahkan)
- ✅ address (ditambahkan dengan nilai kosong)

### Role ID Mapping (Sudah Diperbaiki)
```javascript
const roleIdMap = {
  'ROLE_DOCTOR': 501,        // Benar
  'ROLE_RECEPTIONIST': 503,  // Benar
  'ROLE_ACCOUNTANT': 504     // Benar
}
```

## Kesimpulan

### Masalah
1. ModelMapper meng-override inisialisasi `roles` di User entity
2. Frontend tidak mengirim field wajib (gender, mobileNo, securityQue, securityAns)
3. Role ID mapping salah (sebelumnya DOCTOR = 502, seharusnya 501)

### Solusi
1. ✅ Inisialisasi roles setelah mapping di semua service (Employee, Patient, Doctor)
2. ✅ Tambahkan field wajib di form frontend
3. ✅ Perbaiki role ID mapping
4. ✅ Tambahkan address object (kosong) untuk menghindari null

### Status
✅ **Semua masalah sudah diperbaiki di backend dan frontend**

## Testing

Setelah perbaikan:
1. Restart backend service
2. Test Add Employee dengan semua field diisi
3. Pastikan tidak ada error "roles is null"
4. Pastikan employee berhasil dibuat dengan role yang benar

