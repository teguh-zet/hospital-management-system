package com.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event untuk Audit Logging
 * Mencatat semua aksi penting dalam sistem untuk compliance dan security
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {
	
	/**
	 * Action type: CREATE, UPDATE, DELETE, LOGIN, LOGOUT, ACCESS
	 */
	private String actionType;
	
	/**
	 * Entity type: PATIENT, DOCTOR, APPOINTMENT, PAYMENT, RESOURCE, USER
	 */
	private String entityType;
	
	/**
	 * Entity ID
	 */
	private String entityId;
	
	/**
	 * User ID yang melakukan action
	 */
	private Integer userId;
	
	/**
	 * Username
	 */
	private String username;
	
	/**
	 * User role
	 */
	private String userRole;
	
	/**
	 * Description/Details
	 */
	private String description;
	
	/**
	 * IP Address
	 */
	private String ipAddress;
	
	/**
	 * Request method (GET, POST, PUT, DELETE)
	 */
	private String requestMethod;
	
	/**
	 * Request URL
	 */
	private String requestUrl;
	
	/**
	 * Status: SUCCESS, FAILED
	 */
	private String status;
	
	/**
	 * Error message (jika ada)
	 */
	private String errorMessage;
	
	/**
	 * Timestamp
	 */
	private Long timestamp;
}

