package com.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event untuk Notifikasi (Email, SMS, Push Notification)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
	
	/**
	 * Tipe notifikasi: EMAIL, SMS, PUSH
	 */
	private String notificationType;
	
	/**
	 * Recipient (email atau phone number)
	 */
	private String recipient;
	
	/**
	 * Subject (untuk email)
	 */
	private String subject;
	
	/**
	 * Message body
	 */
	private String message;
	
	/**
	 * Template name (jika menggunakan template)
	 */
	private String templateName;
	
	/**
	 * Data untuk template (key-value pairs)
	 */
	private java.util.Map<String, Object> templateData;
	
	/**
	 * Priority: HIGH, MEDIUM, LOW
	 */
	private String priority;
	
	/**
	 * Timestamp
	 */
	private Long timestamp;
}

