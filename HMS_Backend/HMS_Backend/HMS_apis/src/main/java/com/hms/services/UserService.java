package com.hms.services;

import com.hms.payloads.ChangePasswordDto;
import com.hms.payloads.UserDto;

public interface UserService {
			// get
			UserDto getUser(Integer Id);
			
			UserDto getUserByEmail(String email);
			
			UserDto forgotPass(String email, String password);
			
			UserDto changePassword(Integer userId, ChangePasswordDto changePasswordDto);
			
			UserDto updatePhoto(Integer userId, String photoPath);
}