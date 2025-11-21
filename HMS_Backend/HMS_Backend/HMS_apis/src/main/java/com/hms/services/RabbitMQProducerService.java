package com.hms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.config.RabbitMQConfig;
import com.hms.events.AppointmentEvent;
import com.hms.events.AuditEvent;
import com.hms.events.NotificationEvent;
import com.hms.events.PaymentEvent;
import com.hms.events.ResourceEvent;

/**
 * Service untuk publish events ke RabbitMQ
 * 
 * Logika Bisnis:
 * - Service ini bertanggung jawab untuk mengirim event ke message broker
 * - Event akan di-consume oleh consumer services untuk processing async
 * - Menggunakan Direct Exchange dengan routing keys untuk routing messages
 */
@Service
public class RabbitMQProducerService {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * Publish Appointment Event
	 * 
	 * Use Case:
	 * - Ketika appointment dibuat → trigger notification ke patient & doctor
	 * - Ketika appointment diupdate → update resource availability
	 * - Ketika appointment dibatalkan → release resources
	 */
	public void publishAppointmentEvent(AppointmentEvent event) {
		try {
			logger.info("Publishing appointment event: {} for patient: {}", event.getEventType(),
					event.getPatientId());
			rabbitTemplate.convertAndSend(RabbitMQConfig.HMS_EXCHANGE,
					RabbitMQConfig.APPOINTMENT_ROUTING_KEY, event);
			logger.info("Appointment event published successfully");
		} catch (Exception e) {
			logger.error("Error publishing appointment event: {}", e.getMessage(), e);
			// Tidak throw exception agar tidak mengganggu flow utama
			// Event bisa di-retry atau di-log untuk manual processing
		}
	}

	/**
	 * Publish Notification Event
	 * 
	 * Use Case:
	 * - Email notification untuk appointment confirmation
	 * - SMS reminder untuk appointment
	 * - Push notification untuk update status
	 */
	public void publishNotificationEvent(NotificationEvent event) {
		try {
			logger.info("Publishing notification event: {} to: {}", event.getNotificationType(),
					event.getRecipient());
			rabbitTemplate.convertAndSend(RabbitMQConfig.HMS_EXCHANGE,
					RabbitMQConfig.NOTIFICATION_ROUTING_KEY, event);
			logger.info("Notification event published successfully");
		} catch (Exception e) {
			logger.error("Error publishing notification event: {}", e.getMessage(), e);
		}
	}

	/**
	 * Publish Payment Event
	 * 
	 * Use Case:
	 * - Ketika payment diproses → send receipt email
	 * - Ketika payment gagal → notify patient
	 * - Update payment status di database
	 */
	public void publishPaymentEvent(PaymentEvent event) {
		try {
			logger.info("Publishing payment event: {} for patient: {}", event.getEventType(),
					event.getPatientId());
			rabbitTemplate.convertAndSend(RabbitMQConfig.HMS_EXCHANGE, RabbitMQConfig.PAYMENT_ROUTING_KEY,
					event);
			logger.info("Payment event published successfully");
		} catch (Exception e) {
			logger.error("Error publishing payment event: {}", e.getMessage(), e);
		}
	}

	/**
	 * Publish Resource Event
	 * 
	 * Use Case:
	 * - Ketika bed di-allocate → update resource availability
	 * - Ketika bed di-release → mark as available
	 * - Sync resource status dengan RESOURCE-SERVICE
	 */
	public void publishResourceEvent(ResourceEvent event) {
		try {
			logger.info("Publishing resource event: {} for resource: {}", event.getEventType(),
					event.getResourceId());
			rabbitTemplate.convertAndSend(RabbitMQConfig.HMS_EXCHANGE, RabbitMQConfig.RESOURCE_ROUTING_KEY,
					event);
			logger.info("Resource event published successfully");
		} catch (Exception e) {
			logger.error("Error publishing resource event: {}", e.getMessage(), e);
		}
	}

	/**
	 * Publish Audit Event
	 * 
	 * Use Case:
	 * - Log semua aksi penting untuk compliance
	 * - Security audit trail
	 * - Monitoring dan analytics
	 */
	public void publishAuditEvent(AuditEvent event) {
		try {
			logger.debug("Publishing audit event: {} for entity: {}", event.getActionType(),
					event.getEntityType());
			rabbitTemplate.convertAndSend(RabbitMQConfig.HMS_EXCHANGE, RabbitMQConfig.AUDIT_ROUTING_KEY,
					event);
		} catch (Exception e) {
			logger.error("Error publishing audit event: {}", e.getMessage(), e);
		}
	}
}

