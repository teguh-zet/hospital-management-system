package com.hms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.config.RabbitMQConfig;
import com.hms.events.AppointmentEvent;
import com.hms.events.AuditEvent;
import com.hms.events.NotificationEvent;
import com.hms.events.PaymentEvent;
import com.hms.events.ResourceEvent;

/**
 * Service untuk consume events dari RabbitMQ
 * 
 * Logika Bisnis:
 * - Consumer ini akan listen ke queues dan process events secara async
 * - Setiap consumer method handle specific event type
 * - Error handling dengan retry mechanism (configured di application.yml)
 */
@Service
public class RabbitMQConsumerService {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private AuditLogService auditLogService;

	/**
	 * Consumer untuk Appointment Events
	 * 
	 * Logika:
	 * 1. Ketika appointment CREATED:
	 *    - Kirim email konfirmasi ke patient
	 *    - Kirim notifikasi ke doctor (jika assigned)
	 *    - Log audit event
	 * 
	 * 2. Ketika appointment ADMITTED:
	 *    - Update resource availability
	 *    - Kirim welcome email ke patient
	 * 
	 * 3. Ketika appointment DISCHARGED:
	 *    - Release bed resource
	 *    - Kirim discharge summary email
	 * 
	 * 4. Ketika appointment CANCELLED:
	 *    - Release resources
	 *    - Kirim cancellation email
	 */
	@RabbitListener(queues = RabbitMQConfig.APPOINTMENT_QUEUE)
	public void handleAppointmentEvent(AppointmentEvent event) {
		logger.info("Received appointment event: {} for patient: {}", event.getEventType(),
				event.getPatientId());

		try {
			switch (event.getEventType()) {
			case "CREATED":
				handleAppointmentCreated(event);
				break;
			case "UPDATED":
				handleAppointmentUpdated(event);
				break;
			case "ADMITTED":
				handleAppointmentAdmitted(event);
				break;
			case "DISCHARGED":
				handleAppointmentDischarged(event);
				break;
			case "CANCELLED":
				handleAppointmentCancelled(event);
				break;
			default:
				logger.warn("Unknown appointment event type: {}", event.getEventType());
			}
		} catch (Exception e) {
			logger.error("Error processing appointment event: {}", e.getMessage(), e);
			// Event akan di-retry otomatis oleh RabbitMQ (configured di application.yml)
			throw e; // Re-throw untuk trigger retry
		}
	}

	private void handleAppointmentCreated(AppointmentEvent event) {
		logger.info("Handling appointment created for patient: {}", event.getPatientId());

		// Send notification to patient
		NotificationEvent patientNotification = new NotificationEvent();
		patientNotification.setNotificationType("EMAIL");
		patientNotification.setRecipient(event.getPatientEmail());
		patientNotification.setSubject("Appointment Confirmation - HMS");
		patientNotification.setMessage(String.format(
				"Dear %s,\n\nYour appointment has been confirmed.\n\nDate: %s\nTime: %s\nSymptoms: %s\n\nThank you for choosing HMS.",
				event.getPatientName(), event.getAppointmentDate(), event.getAppointmentTime(),
				event.getSymptoms()));
		patientNotification.setPriority("HIGH");
		patientNotification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(patientNotification);

		// Send notification to doctor if assigned
		if (event.getDoctorId() != null && event.getDoctorEmail() != null) {
			NotificationEvent doctorNotification = new NotificationEvent();
			doctorNotification.setNotificationType("EMAIL");
			doctorNotification.setRecipient(event.getDoctorEmail());
			doctorNotification.setSubject("New Appointment Assigned - HMS");
			doctorNotification.setMessage(String.format(
					"Dear Dr. %s,\n\nYou have a new appointment.\n\nPatient: %s\nDate: %s\nTime: %s\nSymptoms: %s",
					event.getDoctorName(), event.getPatientName(), event.getAppointmentDate(),
					event.getAppointmentTime(), event.getSymptoms()));
			doctorNotification.setPriority("HIGH");
			doctorNotification.setTimestamp(System.currentTimeMillis());
			notificationService.sendNotification(doctorNotification);
		}
	}

	private void handleAppointmentUpdated(AppointmentEvent event) {
		logger.info("Handling appointment updated for patient: {}", event.getPatientId());
		// Implement update notification logic
	}

	private void handleAppointmentAdmitted(AppointmentEvent event) {
		logger.info("Handling appointment admitted for patient: {}", event.getPatientId());

		// Update resource availability
		if (event.getWardId() != null && event.getAllocatedBed() != null) {
			ResourceEvent resourceEvent = new ResourceEvent();
			resourceEvent.setEventType("BED_ALLOCATED");
			resourceEvent.setResourceType("BED");
			resourceEvent.setResourceId(event.getAllocatedBed());
			resourceEvent.setWardId(event.getWardId());
			resourceEvent.setBedNumber(event.getAllocatedBed());
			resourceEvent.setStatus("OCCUPIED");
			resourceEvent.setPatientId(event.getPatientId());
			resourceEvent.setHealthHistoryId(event.getHealthHistoryId());
			resourceEvent.setTimestamp(System.currentTimeMillis());
			// Publish to resource queue (will be handled by resource service)
		}

		// Send welcome email
		NotificationEvent notification = new NotificationEvent();
		notification.setNotificationType("EMAIL");
		notification.setRecipient(event.getPatientEmail());
		notification.setSubject("Welcome to HMS - Admission Confirmed");
		notification.setMessage(String.format(
				"Dear %s,\n\nYou have been admitted to the hospital.\n\nWard: %s\nBed: %s\n\nWe wish you a speedy recovery.",
				event.getPatientName(), event.getWardId(), event.getAllocatedBed()));
		notification.setPriority("HIGH");
		notification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(notification);
	}

	private void handleAppointmentDischarged(AppointmentEvent event) {
		logger.info("Handling appointment discharged for patient: {}", event.getPatientId());

		// Release bed resource
		if (event.getWardId() != null && event.getAllocatedBed() != null) {
			ResourceEvent resourceEvent = new ResourceEvent();
			resourceEvent.setEventType("BED_RELEASED");
			resourceEvent.setResourceType("BED");
			resourceEvent.setResourceId(event.getAllocatedBed());
			resourceEvent.setWardId(event.getWardId());
			resourceEvent.setBedNumber(event.getAllocatedBed());
			resourceEvent.setStatus("AVAILABLE");
			resourceEvent.setPatientId(event.getPatientId());
			resourceEvent.setHealthHistoryId(event.getHealthHistoryId());
			resourceEvent.setTimestamp(System.currentTimeMillis());
		}

		// Send discharge summary email
		NotificationEvent notification = new NotificationEvent();
		notification.setNotificationType("EMAIL");
		notification.setRecipient(event.getPatientEmail());
		notification.setSubject("Discharge Summary - HMS");
		notification.setMessage(String.format(
				"Dear %s,\n\nYou have been discharged from the hospital.\n\nThank you for choosing HMS. We hope you are feeling better.",
				event.getPatientName()));
		notification.setPriority("MEDIUM");
		notification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(notification);
	}

	private void handleAppointmentCancelled(AppointmentEvent event) {
		logger.info("Handling appointment cancelled for patient: {}", event.getPatientId());

		// Send cancellation email
		NotificationEvent notification = new NotificationEvent();
		notification.setNotificationType("EMAIL");
		notification.setRecipient(event.getPatientEmail());
		notification.setSubject("Appointment Cancelled - HMS");
		notification.setMessage(String.format(
				"Dear %s,\n\nYour appointment has been cancelled.\n\nIf you need to reschedule, please contact us.",
				event.getPatientName()));
		notification.setPriority("MEDIUM");
		notification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(notification);
	}

	/**
	 * Consumer untuk Notification Events
	 * 
	 * Logika:
	 * - Process notification berdasarkan type (EMAIL, SMS, PUSH)
	 * - Integrate dengan email service, SMS gateway, atau push notification service
	 */
	@RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
	public void handleNotificationEvent(NotificationEvent event) {
		logger.info("Received notification event: {} to: {}", event.getNotificationType(),
				event.getRecipient());

		try {
			notificationService.sendNotification(event);
		} catch (Exception e) {
			logger.error("Error processing notification event: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Consumer untuk Payment Events
	 * 
	 * Logika:
	 * 1. Ketika payment PROCESSED:
	 *    - Kirim receipt email
	 *    - Update payment status
	 * 
	 * 2. Ketika payment FAILED:
	 *    - Notify patient
	 *    - Log error
	 */
	@RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
	public void handlePaymentEvent(PaymentEvent event) {
		logger.info("Received payment event: {} for patient: {}", event.getEventType(),
				event.getPatientId());

		try {
			switch (event.getEventType()) {
			case "PROCESSED":
				handlePaymentProcessed(event);
				break;
			case "FAILED":
				handlePaymentFailed(event);
				break;
			case "REFUNDED":
				handlePaymentRefunded(event);
				break;
			default:
				logger.warn("Unknown payment event type: {}", event.getEventType());
			}
		} catch (Exception e) {
			logger.error("Error processing payment event: {}", e.getMessage(), e);
			throw e;
		}
	}

	private void handlePaymentProcessed(PaymentEvent event) {
		logger.info("Handling payment processed for patient: {}", event.getPatientId());

		// Send receipt email
		NotificationEvent notification = new NotificationEvent();
		notification.setNotificationType("EMAIL");
		notification.setRecipient(event.getPatientEmail());
		notification.setSubject("Payment Receipt - HMS");
		notification.setMessage(String.format(
				"Dear %s,\n\nPayment received successfully.\n\nAmount: %.2f\nTotal: %.2f\nTransaction ID: %s\n\nThank you for your payment.",
				event.getPatientName(), event.getAmount(), event.getTotalAmount(),
				event.getTransactionId()));
		notification.setPriority("HIGH");
		notification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(notification);
	}

	private void handlePaymentFailed(PaymentEvent event) {
		logger.info("Handling payment failed for patient: {}", event.getPatientId());

		// Notify patient
		NotificationEvent notification = new NotificationEvent();
		notification.setNotificationType("EMAIL");
		notification.setRecipient(event.getPatientEmail());
		notification.setSubject("Payment Failed - HMS");
		notification.setMessage(String.format(
				"Dear %s,\n\nYour payment could not be processed.\n\nPlease try again or contact support.",
				event.getPatientName()));
		notification.setPriority("HIGH");
		notification.setTimestamp(System.currentTimeMillis());
		notificationService.sendNotification(notification);
	}

	private void handlePaymentRefunded(PaymentEvent event) {
		logger.info("Handling payment refunded for patient: {}", event.getPatientId());
		// Implement refund notification logic
	}

	/**
	 * Consumer untuk Resource Events
	 * 
	 * Logika:
	 * - Update resource availability di RESOURCE-SERVICE
	 * - Sync status bed, ward, equipment
	 */
	@RabbitListener(queues = RabbitMQConfig.RESOURCE_QUEUE)
	public void handleResourceEvent(ResourceEvent event) {
		logger.info("Received resource event: {} for resource: {}", event.getEventType(),
				event.getResourceId());

		try {
			// This will be handled by Resource Service
			// For now, just log it
			logger.info("Resource event processed: {} - Status: {}", event.getEventType(),
					event.getStatus());
		} catch (Exception e) {
			logger.error("Error processing resource event: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Consumer untuk Audit Events
	 * 
	 * Logika:
	 * - Save audit log ke database atau file
	 * - Untuk compliance dan security monitoring
	 */
	@RabbitListener(queues = RabbitMQConfig.AUDIT_QUEUE)
	public void handleAuditEvent(AuditEvent event) {
		logger.debug("Received audit event: {} for entity: {}", event.getActionType(),
				event.getEntityType());

		try {
			auditLogService.logAuditEvent(event);
		} catch (Exception e) {
			logger.error("Error processing audit event: {}", e.getMessage(), e);
			// Don't throw for audit events - they shouldn't block main flow
		}
	}
}

