# Implementasi Change Password

## Ringkasan
Dokumen ini menjelaskan implementasi lengkap fitur Change Password yang telah ditambahkan ke Hospital Management System.

---

## Backend Implementation

### 1. ChangePasswordDto
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/payloads/ChangePasswordDto.java`

DTO untuk request change password dengan validasi:
- `currentPassword`: Password saat ini (required)
- `newPassword`: Password baru (required, dengan pattern validation)

**Validasi:**
- Password harus 4-15 karakter
- Harus mengandung minimal 1 huruf besar, 1 huruf kecil, 1 angka, dan 1 karakter khusus

### 2. UserService Interface
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/services/UserService.java`

Menambahkan method:
```java
UserDto changePassword(Integer userId, ChangePasswordDto changePasswordDto);
```

### 3. UserServiceImpl
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/services/impl/UserServiceImpl.java`

Implementasi logic:
1. **Verifikasi User**: Cek apakah user dengan ID tersebut ada
2. **Verifikasi Current Password**: Cek apakah current password benar
3. **Validasi New Password**: Cek apakah new password berbeda dari current password
4. **Update Password**: Encode dan simpan password baru

**Error Handling:**
- `ResourceNotFoundException`: Jika user tidak ditemukan
- `ApiException`: Jika current password salah atau new password sama dengan current password

### 4. UserController
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/controller/UserController.java`

**Endpoint:**
```
PUT /api/users/{userId}/change-password
```

**Security:**
- Protected dengan `@PreAuthorize` untuk semua role (ADMIN, DOCTOR, PATIENT, RECEPTIONIST, ACCOUNTANT)
- Memerlukan authentication token

**Request Body:**
```json
{
  "currentPassword": "CurrentPass123!",
  "newPassword": "NewPass123!"
}
```

**Response:**
- Success: `200 OK` dengan `UserDto`
- Error: `400 BAD REQUEST` dengan `ApiResponse` message

---

## Frontend Implementation

### 1. User Service
**File:** `HMS_Frontend/src/services/user.js`

Menambahkan function:
```javascript
export const changePassword = (userId, changePasswordDto) => {
    return PrivateAxios.put(`/users/${userId}/change-password`, changePasswordDto)
        .then((response) => response.data)
}
```

### 2. Profile Component
**File:** `HMS_Frontend/src/components/Pages/Common/Profile.vue`

**Fitur:**
- Form change password dengan 3 field:
  - Current Password
  - New Password
  - Confirm Password
- Validasi di frontend:
  - Semua field harus diisi
  - New password dan confirm password harus sama
  - Password minimal 4 karakter
  - Password pattern validation (uppercase, lowercase, number, special char)
- Loading state saat proses
- Auto logout setelah password berhasil diubah
- Error handling yang comprehensive

**Flow:**
1. User mengisi form
2. Frontend validasi
3. Call API dengan `changePassword(userId, changePasswordDto)`
4. Jika berhasil:
   - Reset form
   - Tampilkan success message
   - Auto logout setelah 2 detik
   - Redirect ke login page
5. Jika error:
   - Tampilkan error message dari backend

---

## Security Features

### Backend
1. **Password Verification**: Current password harus benar sebelum update
2. **Password Encoding**: Password baru di-encode dengan BCrypt
3. **Authorization**: Hanya user yang login yang bisa change password sendiri
4. **Validation**: Password pattern validation sesuai requirement

### Frontend
1. **Client-side Validation**: Validasi sebelum API call
2. **Password Pattern Check**: Regex validation untuk password strength
3. **Auto Logout**: User harus login kembali setelah change password
4. **Error Handling**: Menampilkan error message yang jelas

---

## API Documentation

### Endpoint
```
PUT /api/users/{userId}/change-password
```

### Headers
```
Authorization: Bearer {token}
Content-Type: application/json
```

### Request Body
```json
{
  "currentPassword": "string (required)",
  "newPassword": "string (required, 4-15 chars, must contain uppercase, lowercase, number, special char)"
}
```

### Success Response (200 OK)
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  ...
}
```

### Error Responses

#### 400 Bad Request - Current Password Incorrect
```json
{
  "message": "Current password is incorrect!",
  "success": true
}
```

#### 400 Bad Request - New Password Same as Current
```json
{
  "message": "New password must be different from current password!",
  "success": true
}
```

#### 400 Bad Request - Validation Error
```json
{
  "newPassword": "Password must be min of 4 characters and max of 15 characters..."
}
```

#### 404 Not Found - User Not Found
```json
{
  "message": "User not found with User id : 999",
  "success": false
}
```

#### 401 Unauthorized
```json
{
  "message": "Unauthorized"
}
```

---

## Testing

### Manual Testing Steps

1. **Test Change Password Success:**
   - Login sebagai user
   - Buka Profile page
   - Isi form dengan:
     - Current Password: password saat ini
     - New Password: password baru yang valid
     - Confirm Password: sama dengan new password
   - Klik "Change Password"
   - Pastikan success message muncul
   - Pastikan auto logout dan redirect ke login
   - Login dengan password baru

2. **Test Current Password Wrong:**
   - Isi current password yang salah
   - Pastikan error message muncul: "Current password is incorrect!"

3. **Test New Password Same as Current:**
   - Isi new password sama dengan current password
   - Pastikan error message muncul: "New password must be different from current password!"

4. **Test Password Validation:**
   - Isi new password yang tidak memenuhi pattern (tanpa uppercase, lowercase, number, atau special char)
   - Pastikan error message muncul sesuai validasi

5. **Test Confirm Password Mismatch:**
   - Isi new password dan confirm password yang berbeda
   - Pastikan error message: "New password dan confirm password tidak sama"

---

## Error Messages

### Frontend Validation
- "Harap lengkapi semua field"
- "New password dan confirm password tidak sama"
- "Password minimal 4 karakter"
- "Password harus mengandung minimal 1 huruf besar, 1 huruf kecil, 1 angka, dan 1 karakter khusus"
- "User ID tidak ditemukan. Silakan login kembali."

### Backend Error Messages
- "Current password is incorrect!"
- "New password must be different from current password!"
- "User not found with User id : {id}"
- Validation error messages dari `@Pattern` annotation

---

## Notes

1. **Password Pattern**: Sesuai dengan requirement di `UserDto`, password harus:
   - Min 4 karakter, max 15 karakter
   - Minimal 1 uppercase letter
   - Minimal 1 lowercase letter
   - Minimal 1 digit
   - Minimal 1 special character (@#$%^&-+=())

2. **Auto Logout**: Setelah password berhasil diubah, user akan otomatis logout setelah 2 detik. Ini untuk security karena token mungkin masih valid.

3. **Token**: Endpoint menggunakan `PrivateAxios` yang otomatis menambahkan JWT token dari localStorage.

4. **Error Handling**: Frontend menangani berbagai format error response dari backend (ApiResponse, validation errors, dll).

---

## Files Modified/Created

### Backend
1. ✅ `ChangePasswordDto.java` - **NEW**
2. ✅ `UserService.java` - **MODIFIED**
3. ✅ `UserServiceImpl.java` - **MODIFIED**
4. ✅ `UserController.java` - **MODIFIED**

### Frontend
1. ✅ `user.js` (services) - **MODIFIED**
2. ✅ `Profile.vue` - **MODIFIED**

---

**Status:** ✅ **COMPLETED**
**Date:** {{ new Date().toLocaleDateString('id-ID') }}

