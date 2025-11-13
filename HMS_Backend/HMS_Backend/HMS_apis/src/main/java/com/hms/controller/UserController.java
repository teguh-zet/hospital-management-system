package com.hms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hms.exceptions.ApiException;
import com.hms.payloads.ChangePasswordDto;
import com.hms.payloads.UserDto;
import com.hms.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Value("${app.upload.dir:${user.home}/hms-uploads}")
	private String uploadDir;
	
		// get
		@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('RECEPTIONIST') or hasRole('ACCOUNTANT')")
		@GetMapping("/{Id}")
		public ResponseEntity<UserDto> getUser(@PathVariable Integer Id) {
			UserDto userDto = this.userService.getUser(Id);
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}
		
		//--------------------------------------------------------------------------------
		
		//forgot Pass
		@GetMapping("/email/{email}")
		public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
			UserDto userDto = this.userService.getUserByEmail(email);
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}
		
		//--------------------------------------------------------------------------------
		
		// Change Password
		@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('RECEPTIONIST') or hasRole('ACCOUNTANT')")
		@PutMapping("/{userId}/change-password")
		public ResponseEntity<UserDto> changePassword(@PathVariable Integer userId, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
			UserDto updatedUser = this.userService.changePassword(userId, changePasswordDto);
			return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
		}
		
		//--------------------------------------------------------------------------------
		
		// Upload/Update Photo
		@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('RECEPTIONIST') or hasRole('ACCOUNTANT')")
		@PostMapping("/{userId}/upload-photo")
		public ResponseEntity<UserDto> uploadPhoto(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
			if (file.isEmpty()) {
				throw new ApiException("File is empty!");
			}
			
			// Validate file type
			String contentType = file.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				throw new ApiException("File must be an image!");
			}
			
			// Validate file size (max 5MB)
			if (file.getSize() > 5 * 1024 * 1024) {
				throw new ApiException("File size must be less than 5MB!");
			}
			
			try {
				// Create upload directory if it doesn't exist
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				
				// Generate unique filename
				String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
				String extension = "";
				if (originalFilename != null && originalFilename.contains(".")) {
					extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				}
				String filename = "user_" + userId + "_" + UUID.randomUUID().toString() + extension;
				
				// Save file
				Path filePath = uploadPath.resolve(filename);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				
				// Get relative path for database storage
				String photoPath = "/uploads/" + filename;
				
				// Update user photo
				UserDto updatedUser = this.userService.updatePhoto(userId, photoPath);
				
				return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
			} catch (IOException e) {
				throw new ApiException("Failed to upload file: " + e.getMessage());
			}
		}
		
		// Delete Photo
		@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('RECEPTIONIST') or hasRole('ACCOUNTANT')")
		@DeleteMapping("/{userId}/photo")
		public ResponseEntity<UserDto> deletePhoto(@PathVariable Integer userId) {
			UserDto updatedUser = this.userService.updatePhoto(userId, null);
			return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
		}
		
		//--------------------------------------------------------------------------------
		
}
