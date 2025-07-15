package com.hms;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import com.hms.entities.Ward;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hms.config.AppConstants;
import com.hms.entities.Role;
import com.hms.entities.User;
import com.hms.entities.Ward;
import com.hms.repository.RoleRepo;
import com.hms.repository.UserRepo;
import com.hms.repository.WardRepo;

@SpringBootApplication
@EnableEurekaClient
public class HmsApisApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(HmsApisApplication.class);
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
private WardRepo wardRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HmsApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// @Override
	// public void run(String... args) throws Exception {

		// 1. Membuat semua role yang dibutuhkan, hanya jika tabel role masih kosong
		// try {
		// 	if (this.roleRepo.count() == 0) {
		// 		Role role_patient = new Role(AppConstants.ROLE_PATIENT, "ROLE_PATIENT");
		// 		Role role_doctor = new Role(AppConstants.ROLE_DOCTOR, "ROLE_DOCTOR");
		// 		Role role_accountant = new Role(AppConstants.ROLE_ACCOUNTANT, "ROLE_ACCOUNTANT");
		// 		Role role_receptionist = new Role(AppConstants.ROLE_RECEPTIONIST, "ROLE_RECEPTIONIST");
		// 		Role role_admin = new Role(AppConstants.ROLE_ADMIN, "ROLE_ADMIN");

		// 		List<Role> roles = List.of(role_patient, role_doctor, role_accountant, role_receptionist, role_admin);
		// 		this.roleRepo.saveAll(roles);
		// 		System.out.println("++++++++++++++ All roles have been created! ++++++++++++++");
		// 	}
		// } catch (Exception e) {
		// 	System.err.println("An error occurred during role creation: " + e.getMessage());
		// 	e.printStackTrace();
		// }

		// // 2. Membuat Admin HANYA JIKA BELUM ADA
		// try {
		// 	if (this.userRepo.findByEmail("admin@gmail.com").isEmpty()) {
				
		// 		// Mengambil ROLE_ADMIN dari database dengan aman
		// 		this.roleRepo.findById(AppConstants.ROLE_ADMIN).ifPresent(adminRole -> {
					
		// 			User adminUser = new User();
		// 			adminUser.setFirstName("admin");
		// 			adminUser.setLastName("admin");
		// 			adminUser.setSecurityQue("admin");
		// 			adminUser.setSecurityAns("admin");
		// 			adminUser.setEmail("admin@gmail.com");
		// 			adminUser.setMobileNo("1234567890");
		// 			adminUser.setPassword(this.passwordEncoder.encode("admin"));
		// 			adminUser.setRoles(Set.of(adminRole));
					
		// 			this.userRepo.save(adminUser);
		// 			System.out.println("++++++++++++++ Default admin user has been created! ++++++++++++++");
		// 		});
		// 	} else {
        //         System.out.println("Admin user already exists. Skipping creation.");
        //     }
		// } catch (Exception e) {
		// 	System.err.println("An error occurred during admin user creation: " + e.getMessage());
		// 	e.printStackTrace();
		// }
	// }
	
	@Override
@Transactional // Pastikan anotasi ini ada
public void run(String... args) throws Exception {

    // 1. Membuat Role
    try {
        if (this.roleRepo.count() == 0) {
            log.info("Table 'role' is empty. Creating initial roles...");
            Role role_patient = new Role(AppConstants.ROLE_PATIENT, "ROLE_PATIENT");
            Role role_doctor = new Role(AppConstants.ROLE_DOCTOR, "ROLE_DOCTOR");
            Role role_accountant = new Role(AppConstants.ROLE_ACCOUNTANT, "ROLE_ACCOUNTANT");
            Role role_receptionist = new Role(AppConstants.ROLE_RECEPTIONIST, "ROLE_RECEPTIONIST");
            Role role_admin = new Role(AppConstants.ROLE_ADMIN, "ROLE_ADMIN");

            List<Role> roles = List.of(role_patient, role_doctor, role_accountant, role_receptionist, role_admin);
            this.roleRepo.saveAll(roles);
            log.info("++++++++++++++ All roles have been created! ++++++++++++++");
        } else {
            log.info("Roles already exist in the database. Skipping creation.");
        }
    } catch (Exception e) {
        log.error("An error occurred during role creation:", e);
    }

    // 2. Membuat Admin
    try {
        String adminEmail = "admin@gmail.com";
        if (this.userRepo.findByEmail(adminEmail).isEmpty()) {
            log.info("Admin user with email '{}' not found. Creating a new one...", adminEmail);
            
            this.roleRepo.findById(AppConstants.ROLE_ADMIN).ifPresentOrElse(adminRole -> {
                
                User adminUser = new User();
                adminUser.setFirstName("admin");
                adminUser.setLastName("admin");
                adminUser.setSecurityQue("admin");
                adminUser.setSecurityAns("admin");
                adminUser.setEmail(adminEmail);
                adminUser.setMobileNo("1234567890");
                adminUser.setPassword(this.passwordEncoder.encode("admin"));
                adminUser.setRoles(Set.of(adminRole));
                
                this.userRepo.save(adminUser);
                log.info("++++++++++++++ Default admin user has been created successfully! ++++++++++++++");

            }, () -> {
                log.error("FATAL: ROLE_ADMIN with ID {} could not be found. Admin user cannot be created.", AppConstants.ROLE_ADMIN);
            });

        } else {
            log.warn("Admin user with email '{}' already exists. Skipping creation.", adminEmail);
        }
    } catch (Exception e) {
        log.error("An error occurred during admin user creation:", e);
    }
    
    // 3. Membuat Ward bawaan (TAMBAHKAN BLOK INI DI SINI)
    // KODE YANG BENAR
try {
    if (this.wardRepo.count() == 0) {
        log.info("Table 'ward' is empty. Creating initial wards...");

        // Membuat General Ward
        Ward generalWard = new Ward();
        generalWard.setWardType("General Ward"); // Menggunakan setWardType
        generalWard.setWardCharges(500000);   // Menambahkan biaya bangsal (contoh)

        // Membuat ICU Ward
        Ward icuWard = new Ward();
        icuWard.setWardType("ICU"); // Menggunakan setWardType
        icuWard.setWardCharges(1500000); // Menambahkan biaya bangsal (contoh)

        this.wardRepo.saveAll(List.of(generalWard, icuWard));
        log.info("++++++++++++++ Default wards have been created! ++++++++++++++");
    } else {
        log.info("Wards already exist. Skipping creation.");
    }
} catch (Exception e) {
    log.error("An error occurred during ward creation:", e);
}
    
    log.info("CommandLineRunner execution finished.");
}
}