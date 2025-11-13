package com.hms.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.entities.User;
import com.hms.exceptions.ApiException;
import com.hms.exceptions.ResourceNotFoundException;
import com.hms.payloads.ChangePasswordDto;
import com.hms.payloads.UserDto;
import com.hms.repository.UserRepo;
import com.hms.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${app.upload.dir:${user.home}/hms-uploads}")
	private String uploadDir;

	@Override
	public UserDto getUser(Integer Id) {
		User user = this.userRepo.findById(Id).orElseThrow(() -> new ResourceNotFoundException("User", "User id", Id));
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = this.userRepo.findByEmail(email).orElseThrow();
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto forgotPass(String email, String password) {
		
		User user = this.userRepo.findByEmail(email).orElseThrow();
		user.setPassword(this.passwordEncoder.encode(password));
		
		User updateduser= this.userRepo.save(user);
		return this.modelMapper.map(updateduser, UserDto.class);
	}

	@Override
	public UserDto changePassword(Integer userId, ChangePasswordDto changePasswordDto) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
		
		// Verify current password
		if (!this.passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
			throw new ApiException("Current password is incorrect!");
		}
		
		// Verify security answer (case-insensitive)
		if (!user.getSecurityAns().equalsIgnoreCase(changePasswordDto.getSecurityAnswer().trim())) {
			throw new ApiException("Security answer is incorrect!");
		}
		
		// Check if new password is same as current password
		if (this.passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
			throw new ApiException("New password must be different from current password!");
		}
		
		// Update password
		user.setPassword(this.passwordEncoder.encode(changePasswordDto.getNewPassword()));
		User updatedUser = this.userRepo.save(user);
		
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto updatePhoto(Integer userId, String photoPath) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
		
		// Delete old photo if exists and new photo is being set
		if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
			try {
				String oldPhotoPath = user.getPhoto().replace("/uploads/", "");
				Path oldFilePath = Paths.get(uploadDir).resolve(oldPhotoPath);
				if (Files.exists(oldFilePath)) {
					Files.delete(oldFilePath);
				}
			} catch (IOException e) {
				// Log error but continue
				System.err.println("Failed to delete old photo: " + e.getMessage());
			}
		}
		
		// Update photo path (can be null to remove photo)
		user.setPhoto(photoPath);
		User updatedUser = this.userRepo.save(user);
		
		return this.modelMapper.map(updatedUser, UserDto.class);
	}
}
