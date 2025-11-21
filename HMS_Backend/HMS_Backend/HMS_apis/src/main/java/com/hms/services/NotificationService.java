package com.hms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hms.events.NotificationEvent;

/**
 * Service untuk handle notifications
 * 
 * Logika Bisnis:
 * - Service ini akan mengirim notifikasi berdasarkan type (EMAIL, SMS, PUSH)
 * - Untuk production, integrate dengan:
 *   - Email: JavaMailSender, SendGrid, AWS SES
 *   - SMS: Twilio, AWS SNS
 *   - Push: Firebase Cloud Messaging
 * 
 * Saat ini implementasi hanya logging, untuk production perlu di-extend
 */
@Service
public class NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	/**
	 * Send notification berdasarkan type
	 */
	public void sendNotification(NotificationEvent event) {
		logger.info("Sending {} notification to: {}", event.getNotificationType(), event.getRecipient());

		switch (event.getNotificationType()) {
		case "EMAIL":
			sendEmail(event);
			break;
		case "SMS":
			sendSMS(event);
			break;
		case "PUSH":
			sendPushNotification(event);
			break;
		default:
			logger.warn("Unknown notification type: {}", event.getNotificationType());
		}
	}

	/**
	 * Send Email Notification
	 * 
	 * TODO: Integrate dengan email service (JavaMailSender, SendGrid, dll)
	 */
	private void sendEmail(NotificationEvent event) {
		logger.info("=== EMAIL NOTIFICATION ===");
		logger.info("To: {}", event.getRecipient());
		logger.info("Subject: {}", event.getSubject());
		logger.info("Message: {}", event.getMessage());
		logger.info("Priority: {}", event.getPriority());
		logger.info("==========================");

		// TODO: Implement actual email sending
		// Example:
		// JavaMailSender mailSender = ...;
		// SimpleMailMessage message = new SimpleMailMessage();
		// message.setTo(event.getRecipient());
		// message.setSubject(event.getSubject());
		// message.setText(event.getMessage());
		// mailSender.send(message);
	}

	/**
	 * Send SMS Notification
	 * 
	 * TODO: Integrate dengan SMS gateway (Twilio, AWS SNS, dll)
	 */
	private void sendSMS(NotificationEvent event) {
		logger.info("=== SMS NOTIFICATION ===");
		logger.info("To: {}", event.getRecipient());
		logger.info("Message: {}", event.getMessage());
		logger.info("Priority: {}", event.getPriority());
		logger.info("========================");

		// TODO: Implement actual SMS sending
		// Example with Twilio:
		// Twilio.init(accountSid, authToken);
		// Message.creator(new PhoneNumber(event.getRecipient()), ...).create();
	}

	/**
	 * Send Push Notification
	 * 
	 * TODO: Integrate dengan push notification service (FCM, APNS, dll)
	 */
	private void sendPushNotification(NotificationEvent event) {
		logger.info("=== PUSH NOTIFICATION ===");
		logger.info("To: {}", event.getRecipient());
		logger.info("Message: {}", event.getMessage());
		logger.info("Priority: {}", event.getPriority());
		logger.info("=========================");

		// TODO: Implement actual push notification
		// Example with FCM:
		// FirebaseMessaging.getInstance().send(message);
	}
}

