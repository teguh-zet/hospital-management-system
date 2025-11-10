# Hospital Management System - Frontend (Vue.js)

Aplikasi Hospital Management System yang telah dikonversi dari React ke Vue.js dan disesuaikan dengan backend.

## Teknologi yang Digunakan

- **Vue.js 3** - Framework JavaScript
- **Vue Router** - Routing untuk aplikasi
- **Pinia** - State management
- **Vite** - Build tool dan development server
- **Bootstrap 5** - CSS framework
- **Bootstrap Icons** - Icon library
- **Axios** - HTTP client
- **Vue Toastification** - Notifikasi toast

## Prasyarat

1. **Backend harus berjalan terlebih dahulu:**
   - Eureka Server (port 8761)
   - Config Server (port 9093)
   - API Gateway (port 9090)
   - HMS Service (port 9091)
   - Resource Service (port 9092)

2. **Database MySQL:**
   - Database: `hospital_management_backend`
   - Pastikan database sudah dibuat dan backend bisa terhubung

## Instalasi

1. Install dependencies:
```bash
cd HMS_Frontend
npm install
```

2. Pastikan backend sudah berjalan di:
   - API Gateway: `http://localhost:9090`

3. Jalankan development server:
```bash
npm run dev
```

4. Build untuk production:
```bash
npm run build
```

5. Preview build production:
```bash
npm run preview
```

## Struktur Proyek

```
src/
├── components/          # Komponen Vue
│   ├── Base/           # Komponen base/layout
│   ├── Pages/          # Halaman-halaman aplikasi
│   ├── Navbar/         # Navigation bar
│   ├── Footer/         # Footer
│   └── ...
├── router/             # Konfigurasi Vue Router
├── stores/             # Pinia stores
├── services/           # API services
│   ├── user.js         # Authentication services
│   ├── admin.js        # Admin services
│   ├── doctor.js       # Doctor services
│   ├── patient.js      # Patient services
│   ├── receptionist.js # Receptionist services
│   ├── accountant.js   # Accountant services
│   └── resources.js   # Resources services
├── utils/              # Utility functions
│   ├── auth.js        # Authentication utilities
│   └── axios.js       # Axios configuration
├── config/             # Configuration files
│   └── api.js         # API configuration
└── main.js            # Entry point aplikasi
```

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/register` - Register patient
- `PUT /api/v1/auth/email/{email}/forgot/{password}` - Forgot password
- `GET /api/users/email/{email}` - Get user by email

### Admin
- `POST /api/employee/admin` - Create admin
- `POST /api/employee/create/{Id}` - Create employee
- `GET /api/employee` - Get all employees
- `GET /api/patients` - Get all patients

### Doctor
- `GET /api/doctors` - Get all doctors
- `GET /api/doctor/{doctorId}/patients` - Get patients by doctor
- `PUT /api/doctor/{docId}/schedule/{days}` - Select schedule

### Patient
- `POST /api/patients/{patientId}/healthHistory` - Add appointment
- `GET /api/patient/{patientId}/appointmenthistory` - Get appointment history
- `GET /api/patient/{patientId}/healthhistory` - Get health history
- `DELETE /api/healthhistory/{Id}` - Cancel appointment

### Receptionist
- `GET /api/receptionist/patients` - Get all patients
- `GET /api/receptionist/patients/admit` - Get patients for admit
- `GET /api/receptionist/patients/discharge` - Get patients for discharge
- `PUT /api/patients/{patientId}/doctor/{doctorId}` - Appoint doctor
- `PUT /api/healthhistory/ward/{wardId}` - Admit patient
- `PUT /api/healthhistory/{Id}/discharge` - Discharge patient

### Accountant
- `GET /api/appointment/patients` - Get patients after appointment
- `GET /api/patients/{patientId}` - Get patient by ID
- `PUT /api/healthhistory/{Id}/amount/{amt}` - Update payment

### Resources
- `POST /api/resources/add` - Create resource
- `GET /api/resources` - Get all resources
- `GET /api/resources/{Id}` - Get resource by ID
- `PUT /api/resources/{Id}` - Update resource
- `DELETE /api/resources/{Id}` - Delete resource

## Role IDs

- **ADMIN**: 500
- **DOCTOR**: 501
- **PATIENT**: 502
- **RECEPTIONIST**: 503
- **ACCOUNTANT**: 504

## Konfigurasi

### Base URL
Default base URL adalah `http://localhost:9090/api/`. Jika backend berjalan di port berbeda, edit file:
- `src/utils/axios.js` - Ubah `BASE_URL`
- `src/config/api.js` - Ubah `BASE_URL` dan `RESOURCE_URL`

## Troubleshooting

### 1. CORS Error
Pastikan backend sudah mengkonfigurasi CORS dengan benar. Backend sudah dikonfigurasi untuk allow all origins.

### 2. 401 Unauthorized
- Pastikan token masih valid
- Token akan otomatis dihapus jika expired dan redirect ke login

### 3. 403 Forbidden
- Pastikan user memiliki role yang tepat untuk mengakses endpoint
- Check role di localStorage setelah login

### 4. Connection Error
- Pastikan backend sudah berjalan
- Check API Gateway di `http://localhost:9090`
- Pastikan Eureka Server sudah running

### 5. Port Already in Use
Jika port 3000 sudah digunakan, Vite akan otomatis menggunakan port lain. Check terminal untuk melihat port yang digunakan.

## Status Konversi

### ✅ Sudah Dikonversi dan Disesuaikan:
- Struktur dasar aplikasi (router, store, utils)
- Komponen authentication (Login, Signup, ForgotPass)
- Komponen public (Home, About, Services, Contact)
- Base components (Base, BaseCart, BaseCa, Navbar, Footer)
- Routing dan authentication guards
- Semua service files sesuai dengan backend endpoints
- Axios interceptors untuk error handling

### ⚠️ Perlu Dikonversi (Placeholder sudah dibuat):
- Dashboard komponen (Patient, Admin, Doctor, Receptionist, Accountant)
- Komponen halaman detail (Appointment, HealthHistory, dll)
- Komponen manajemen (AddEmployee, RemoveEmployee, dll)
- Komponen resources (GetResources, AddResources, dll)

## Catatan Penting

1. **Backend API**: Pastikan backend API berjalan di `http://localhost:9090/api/`
2. **Token Management**: Token disimpan di localStorage dengan key 'data'
3. **Role-based Routing**: Setelah login, user akan di-redirect sesuai role
4. **Error Handling**: Semua error API akan ditangani oleh axios interceptors

## Development

Untuk development, pastikan:
1. Backend services sudah running
2. Database sudah terhubung
3. Eureka Server sudah running untuk service discovery
4. API Gateway sudah running

## Production Build

```bash
npm run build
```

File build akan berada di folder `dist/`. Deploy folder ini ke web server seperti Nginx atau Apache.
