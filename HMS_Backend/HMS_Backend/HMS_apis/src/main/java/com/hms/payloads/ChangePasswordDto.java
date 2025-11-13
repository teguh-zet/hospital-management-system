package com.hms.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDto {
	
	@NotEmpty
	@JsonProperty(access = Access.WRITE_ONLY)
	private String currentPassword;
	
	@NotEmpty
	@JsonProperty(access = Access.WRITE_ONLY)
	@Pattern(regexp = "^((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{4,15})$", 
		message = "Password must be min of 4 characters and max of 15 characters and password must contain atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit")
	private String newPassword;
	
	@NotEmpty
	@JsonProperty(access = Access.WRITE_ONLY)
	private String securityAnswer;
}

