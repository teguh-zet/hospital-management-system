import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '../utils/auth'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../components/Home/Home.vue')
  },
  {
    path: '/home',
    name: 'HomeAlt',
    component: () => import('../components/Home/Home.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../components/About/About.vue')
  },
  {
    path: '/services',
    name: 'Services',
    component: () => import('../components/Services/Services.vue')
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../components/Contact/Contact.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../components/Pages/Login/Login.vue')
  },
  {
    path: '/forgot',
    name: 'ForgotPass',
    component: () => import('../components/Pages/Login/ForgotPass.vue')
  },
  {
    path: '/signUp',
    name: 'Signup',
    component: () => import('../components/Pages/Login/Signup.vue')
  },
  {
    path: '/user',
    component: () => import('../components/Base/PrivateRoute.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'patient/dashboard',
        name: 'PatientDashboard',
        component: () => import('../components/Pages/Patient/PatientDashboard.vue')
      },
      {
        path: 'patient/appointment',
        name: 'Appointment',
        component: () => import('../components/Pages/Patient/Appointment.vue')
      },
      {
        path: 'patient/appoHistory',
        name: 'AppointmentHistory',
        component: () => import('../components/Pages/Patient/AppointmentHistory.vue')
      },
      {
        path: 'patient/healthHistory',
        name: 'HealthHistory',
        component: () => import('../components/Pages/Patient/HealthHistory.vue')
      },
      {
        path: 'admin/dashboard',
        name: 'AdminDashboard',
        component: () => import('../components/Pages/Admin/AdminDashboard.vue')
      },
      {
        path: 'admin/allEmployee',
        name: 'AllEmployee',
        component: () => import('../components/Pages/Admin/AllEmployee.vue')
      },
      {
        path: 'admin/allPatient',
        name: 'AllPatient',
        component: () => import('../components/Pages/Admin/AllPatient.vue')
      },
      {
        path: 'admin/addEmp',
        name: 'AddEmployee',
        component: () => import('../components/Pages/Admin/AddEmployee.vue')
      },
      {
        path: 'admin/rmEmp',
        name: 'RemoveEmployee',
        component: () => import('../components/Pages/Admin/RemoveEmployee.vue')
      },
      {
        path: 'admin/resources',
        name: 'AdminGetResources',
        component: () => import('../components/Pages/HospitalResources/AdminGetResources.vue')
      },
      {
        path: 'admin/addResources',
        name: 'AddResources',
        component: () => import('../components/Pages/HospitalResources/AddResources.vue')
      },
      {
        path: 'doctor/dashboard',
        name: 'DoctorDashboard',
        component: () => import('../components/Pages/Doctor/DoctorDashboard.vue')
      },
      {
        path: 'doctor/appointmentList',
        name: 'AppointPatientList',
        component: () => import('../components/Pages/Doctor/AppointPatientList.vue')
      },
      {
        path: 'doctor/resources',
        name: 'GetResources',
        component: () => import('../components/Pages/HospitalResources/GetResources.vue')
      },
      {
        path: 'doctor/selectSchedule',
        name: 'Schedule',
        component: () => import('../components/Pages/Doctor/Schedule.vue')
      },
      {
        path: 'receptionist/dashboard',
        name: 'ReceptionistDashboard',
        component: () => import('../components/Pages/Receptionist/ReceptionistDashboard.vue')
      },
      {
        path: 'receptionist/appointmentList',
        name: 'AppointmentList',
        component: () => import('../components/Pages/Receptionist/AppointmentList.vue')
      },
      {
        path: 'receptionist/admitList',
        name: 'AdmitPatient',
        component: () => import('../components/Pages/Receptionist/AdmitPatient.vue')
      },
      {
        path: 'receptionist/discharge',
        name: 'DischargePatient',
        component: () => import('../components/Pages/Receptionist/DischargePatient.vue')
      },
      {
        path: 'receptionist/resource',
        name: 'GetResourcesReceptionist',
        component: () => import('../components/Pages/HospitalResources/GetResourcesReceptionist.vue')
      },
      {
        path: 'accountant/dashboard',
        name: 'AccountantDashboard',
        component: () => import('../components/Pages/Accountant/AccountantDashboard.vue')
      },
      {
        path: 'accountant/PatientAccountList',
        name: 'PatientAccountList',
        component: () => import('../components/Pages/Accountant/PatientAccountList.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !isLoggedIn()) {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router

