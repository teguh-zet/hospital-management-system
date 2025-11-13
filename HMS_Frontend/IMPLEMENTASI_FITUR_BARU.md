# Implementasi Fitur Baru: Security Answer & Photo Upload

## Ringkasan
Dokumen ini menjelaskan implementasi dua fitur baru:
1. **Security Answer Verification** untuk Change Password
2. **Photo Upload/Edit** untuk Employee dan User Profile

---

## 1. Security Answer Verification untuk Change Password

### Backend Changes

#### ChangePasswordDto.java
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/payloads/ChangePasswordDto.java`

Menambahkan field `securityAnswer`:
```java
@NotEmpty
@JsonProperty(access = Access.WRITE_ONLY)
private String securityAnswer;
```

#### UserServiceImpl.java
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/services/impl/UserServiceImpl.java`

Menambahkan verifikasi security answer (case-insensitive):
```java
// Verify security answer (case-insensitive)
if (!user.getSecurityAns().equalsIgnoreCase(changePasswordDto.getSecurityAnswer().trim())) {
    throw new ApiException("Security answer is incorrect!");
}
```

### Frontend Changes

#### Profile.vue
**File:** `HMS_Frontend/src/components/Pages/Common/Profile.vue`

1. Menambahkan field Security Answer di form:
```vue
<div class="mb-3">
  <label for="securityAnswer" class="form-label">Security Answer</label>
  <input
    type="text"
    class="form-control"
    id="securityAnswer"
    v-model="passwordForm.securityAnswer"
    :placeholder="`Answer: ${userStore.currentUser?.securityQue || 'Security Question'}`"
    required
  />
  <small class="text-muted">Masukkan jawaban dari security question Anda</small>
</div>
```

2. Menambahkan `securityAnswer` ke `passwordForm` dan `changePasswordDto`

### Flow
1. User mengisi Current Password, New Password, Confirm Password, dan **Security Answer**
2. Backend memverifikasi:
   - Current password benar
   - **Security answer benar** (case-insensitive)
   - New password berbeda dari current password
3. Jika semua valid, password diupdate

---

## 2. Photo Upload/Edit Feature

### Backend Changes

#### User Entity & DTO
**Files:**
- `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/entities/User.java`
- `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/payloads/UserDto.java`

Menambahkan field `photo`:
```java
@Column(name = "photo", length = 500)
private String photo; // Stores path like "/uploads/user_1_uuid.jpg"
```

#### UserService & UserServiceImpl
**Files:**
- `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/services/UserService.java`
- `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/services/impl/UserServiceImpl.java`

Menambahkan method:
```java
UserDto updatePhoto(Integer userId, String photoPath);
```

**Logic:**
- Hapus foto lama jika ada
- Update path foto baru di database
- Support null untuk menghapus foto

#### UserController
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/controller/UserController.java`

**Endpoints:**
1. **Upload/Update Photo:**
   ```
   POST /api/users/{userId}/upload-photo
   Content-Type: multipart/form-data
   Body: file (MultipartFile)
   ```

2. **Delete Photo:**
   ```
   DELETE /api/users/{userId}/photo
   ```

**Validasi:**
- File tidak boleh kosong
- Harus berupa image (contentType starts with "image/")
- Max size: 5MB
- Generate unique filename: `user_{userId}_{UUID}.{extension}`

#### WebConfig.java (NEW)
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/java/com/hms/config/WebConfig.java`

Konfigurasi static resource handler untuk serve uploaded files:
```java
registry.addResourceHandler("/uploads/**")
    .addResourceLocations("file:" + uploadPathStr + "/");
```

#### application.properties
**File:** `HMS_Backend/HMS_Backend/HMS_apis/src/main/resources/application.properties`

Menambahkan konfigurasi:
```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

app.upload.dir=${user.home}/hms-uploads
```

### Frontend Changes

#### user.js (Services)
**File:** `HMS_Frontend/src/services/user.js`

Menambahkan functions:
```javascript
export const uploadPhoto = (userId, formData) => {
    return PrivateAxios.post(`/users/${userId}/upload-photo`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then((response) => response.data)
}

export const removePhoto = (userId) => {
    return PrivateAxios.delete(`/users/${userId}/photo`).then((response) => response.data)
}
```

#### Profile.vue
**File:** `HMS_Frontend/src/components/Pages/Common/Profile.vue`

**Fitur:**
1. **Display Photo:**
   - Menampilkan foto jika ada
   - Fallback ke icon jika tidak ada foto
   - Circular image dengan border

2. **Upload Photo:**
   - File input dengan accept="image/*"
   - Preview sebelum upload
   - Validasi file type dan size
   - Button "Upload Photo" atau "Change Photo"

3. **Remove Photo:**
   - Button "Remove" jika ada foto
   - Konfirmasi sebelum hapus

**Functions:**
- `getPhotoUrl(photoPath)`: Generate URL untuk foto
- `handlePhotoUpload(event)`: Handle file upload
- `handleRemovePhoto()`: Handle remove photo

#### AddEmployee.vue
**File:** `HMS_Frontend/src/components/Pages/Admin/AddEmployee.vue`

**Fitur:**
1. **File Upload Input:**
   - Input file untuk upload foto (optional)
   - Preview foto sebelum submit
   - Validasi file type dan size

2. **Upload Flow:**
   - Create employee terlebih dahulu
   - Jika berhasil dan ada foto, upload foto menggunakan user ID dari response
   - Jika upload foto gagal, employee tetap dibuat (warning message)

**Functions:**
- `handlePhotoChange(event)`: Handle file selection dan preview
- `removePhotoPreview()`: Remove preview dan reset file input

---

## File Structure

### Backend
```
HMS_Backend/HMS_Backend/HMS_apis/
├── src/main/java/com/hms/
│   ├── entities/
│   │   └── User.java (MODIFIED - added photo field)
│   ├── payloads/
│   │   ├── ChangePasswordDto.java (MODIFIED - added securityAnswer)
│   │   └── UserDto.java (MODIFIED - added photo field)
│   ├── services/
│   │   ├── UserService.java (MODIFIED - added updatePhoto)
│   │   └── impl/
│   │       └── UserServiceImpl.java (MODIFIED - added changePassword securityAnswer check & updatePhoto)
│   ├── controller/
│   │   └── UserController.java (MODIFIED - added upload/delete photo endpoints)
│   └── config/
│       └── WebConfig.java (NEW - static resource handler)
└── src/main/resources/
    └── application.properties (MODIFIED - multipart config)
```

### Frontend
```
HMS_Frontend/src/
├── services/
│   └── user.js (MODIFIED - added uploadPhoto, removePhoto)
└── components/Pages/
    ├── Common/
    │   └── Profile.vue (MODIFIED - added photo upload/edit & security answer)
    └── Admin/
        └── AddEmployee.vue (MODIFIED - added photo upload)
```

---

## API Endpoints

### 1. Change Password (Updated)
```
PUT /api/users/{userId}/change-password
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "currentPassword": "CurrentPass123!",
  "newPassword": "NewPass123!",
  "securityAnswer": "answer to security question"
}

Response: 200 OK
{
  "id": 1,
  "firstName": "John",
  ...
}
```

**Error Responses:**
- `400 Bad Request`: "Security answer is incorrect!"
- `400 Bad Request`: "Current password is incorrect!"
- `400 Bad Request`: "New password must be different from current password!"

### 2. Upload Photo
```
POST /api/users/{userId}/upload-photo
Authorization: Bearer {token}
Content-Type: multipart/form-data

Request Body:
file: [image file]

Response: 200 OK
{
  "id": 1,
  "photo": "/uploads/user_1_uuid.jpg",
  ...
}
```

**Error Responses:**
- `400 Bad Request`: "File is empty!"
- `400 Bad Request`: "File must be an image!"
- `400 Bad Request`: "File size must be less than 5MB!"

### 3. Delete Photo
```
DELETE /api/users/{userId}/photo
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "photo": null,
  ...
}
```

---

## Upload Directory

**Default Location:** `${user.home}/hms-uploads`

**Contoh:**
- Windows: `C:\Users\{username}\hms-uploads\`
- Linux/Mac: `~/hms-uploads/`

**Custom Location:** Set di `application.properties`:
```properties
app.upload.dir=/path/to/your/uploads
```

**File Naming:** `user_{userId}_{UUID}.{extension}`

**Example:** `user_1_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg`

---

## Security Features

### Change Password
1. ✅ Current password verification
2. ✅ **Security answer verification** (case-insensitive)
3. ✅ New password must be different from current
4. ✅ Password pattern validation
5. ✅ Auto logout after password change

### Photo Upload
1. ✅ File type validation (image only)
2. ✅ File size limit (5MB)
3. ✅ Unique filename generation (UUID)
4. ✅ Old photo deletion when updating
5. ✅ Authorization required (user must be logged in)
6. ✅ User can only upload/delete their own photo (via userId)

---

## Testing

### Test Security Answer Verification

1. **Test dengan Security Answer Benar:**
   - Login sebagai user
   - Buka Profile
   - Isi form change password dengan security answer yang benar
   - Pastikan password berhasil diubah

2. **Test dengan Security Answer Salah:**
   - Isi security answer yang salah
   - Pastikan error: "Security answer is incorrect!"

3. **Test Case-Insensitive:**
   - Security answer di database: "Answer123"
   - Input: "answer123" atau "ANSWER123"
   - Pastikan tetap berhasil

### Test Photo Upload

1. **Test Upload Photo di Profile:**
   - Login sebagai user
   - Buka Profile
   - Klik "Upload Photo"
   - Pilih gambar (JPG, PNG, atau GIF)
   - Pastikan foto tampil setelah upload
   - Pastikan foto tersimpan di upload directory

2. **Test Upload Photo saat Create Employee:**
   - Login sebagai Admin
   - Buka Add Employee
   - Isi semua field
   - Pilih foto (optional)
   - Submit form
   - Pastikan employee dibuat dan foto terupload

3. **Test Change Photo:**
   - Upload foto pertama
   - Upload foto kedua
   - Pastikan foto pertama terhapus dari disk
   - Pastikan foto kedua tampil

4. **Test Remove Photo:**
   - Klik "Remove" pada foto
   - Konfirmasi
   - Pastikan foto terhapus dari database dan disk

5. **Test Validasi:**
   - Upload file non-image → Error: "File must be an image!"
   - Upload file > 5MB → Error: "File size must be less than 5MB!"
   - Upload file kosong → Error: "File is empty!"

---

## Error Messages

### Change Password
- "Harap lengkapi semua field" - Jika ada field yang kosong
- "Security answer is incorrect!" - Jika security answer salah
- "Current password is incorrect!" - Jika current password salah
- "New password must be different from current password!" - Jika new password sama dengan current

### Photo Upload
- "File harus berupa gambar" - Jika file bukan image
- "Ukuran file maksimal 5MB" - Jika file > 5MB
- "File is empty!" - Jika tidak ada file yang dipilih
- "Failed to upload file" - Jika terjadi error saat save file

---

## Notes

1. **Upload Directory:**
   - Directory akan dibuat otomatis jika belum ada
   - Pastikan aplikasi memiliki permission write di directory tersebut

2. **Photo URL:**
   - Photo disimpan sebagai path relatif: `/uploads/filename.jpg`
   - Frontend mengkonversi ke full URL: `{baseURL}/uploads/filename.jpg`
   - Base URL diambil dari axios config

3. **File Cleanup:**
   - Foto lama otomatis dihapus saat upload foto baru
   - Foto dihapus saat user remove photo
   - Jika delete file gagal, hanya log error (tidak fail operation)

4. **Security Answer:**
   - Case-insensitive comparison
   - Trim whitespace sebelum compare
   - Required field di form

5. **Multipart Configuration:**
   - Max file size: 10MB (configurable)
   - Max request size: 10MB (configurable)
   - Enabled by default di Spring Boot

---

## Migration Notes

### Database
Field `photo` akan otomatis ditambahkan ke table `users` saat aplikasi restart (karena `ddl-auto=update`).

**SQL Manual (jika perlu):**
```sql
ALTER TABLE users ADD COLUMN photo VARCHAR(500);
```

### File System
Pastikan directory upload ada dan writable:
```bash
# Linux/Mac
mkdir -p ~/hms-uploads
chmod 755 ~/hms-uploads

# Windows
# Directory akan dibuat otomatis di C:\Users\{username}\hms-uploads\
```

---

**Status:** ✅ **COMPLETED**
**Date:** {{ new Date().toLocaleDateString('id-ID') }}

