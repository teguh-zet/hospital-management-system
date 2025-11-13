# Analisis Error: "Cannot invoke 'java.util.Set.add(Object)' because 'this.roles' is null"

## Root Cause Analysis

### Masalah Utama
Error terjadi karena field `roles` di entity `User` menjadi `null` setelah mapping dari `UserDto` ke `User` menggunakan ModelMapper.

### Alur Masalah

1. **Frontend mengirim data:**
   ```javascript
   user: {
     firstName: "...",
     // ... field lainnya
     // roles TIDAK dikirim (null)
   }
   ```

2. **Backend menerima UserDto:**
   - `UserDto.roles` = `null` (karena tidak dikirim dari frontend)

3. **ModelMapper melakukan mapping:**
   ```java
   User user = this.modelMapper.map(userDto, User.class);
   ```
   - ModelMapper melihat `UserDto.roles = null`
   - ModelMapper set `User.roles = null` (meng-override inisialisasi `new HashSet<>()`)

4. **Backend mencoba add role:**
   ```java
   user.addRole(role); // ERROR: roles masih null!
   ```

### Kode yang Bermasalah

**User.java (Line 93):**
```java
private Set<Role> roles = new HashSet<>(); // Inisialisasi default
```

**UserDto.java (Line 50):**
```java
@JsonInclude(value = Include.NON_NULL)
private Set<Role> roles; // Bisa null
```

**EmployeeServiceImpl.java (Line 88-94):**
```java
User user = this.modelMapper.map(userDto, User.class);
user.setPassword(this.passwordEncoder.encode(user.getPassword()));
user.setAddress(null);
Role role = this.roleRepo.findById(Id)
    .orElseThrow((() -> new ResourceNotFoundException("Role", "Role id", 0)));

user.addRole(role); // ERROR: roles masih null setelah mapping!
```

## Solusi

### Solusi 1: Inisialisasi roles setelah mapping (Recommended)
Tambahkan inisialisasi roles setelah mapping di backend:

```java
User user = this.modelMapper.map(userDto, User.class);
if (user.getRoles() == null) {
    user.setRoles(new HashSet<>());
}
user.setPassword(this.passwordEncoder.encode(user.getPassword()));
// ... rest of code
```

### Solusi 2: Konfigurasi ModelMapper untuk skip null
Konfigurasi ModelMapper untuk tidak meng-override field yang sudah diinisialisasi jika source null.

### Solusi 3: Frontend mengirim roles (tidak recommended)
Frontend bisa mengirim roles, tapi ini tidak aman karena role harus ditentukan oleh backend.

## Rekomendasi

**Gunakan Solusi 1** karena:
- Paling aman (role tetap ditentukan oleh backend)
- Tidak perlu perubahan di frontend
- Mengatasi masalah di root cause
- Konsisten dengan business logic

