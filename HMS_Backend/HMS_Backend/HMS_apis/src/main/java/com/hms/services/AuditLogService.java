package com.hms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hms.events.AuditEvent;

/**
 * Service untuk handle audit logging
 * 
 * Logika Bisnis:
 * - Service ini akan menyimpan audit log untuk compliance dan security
 * - Untuk production, bisa save ke:
 *   - Database (audit_log table)
 *   - File system (log files)
 *   - External logging service (ELK stack, CloudWatch, dll)
 * 
 * Saat ini implementasi hanya logging, untuk production perlu di-extend
 */
@Service
public class AuditLogService {

	private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

	/**
	 * Log audit event
	 */
	public void logAuditEvent(AuditEvent event) {
		logger.info("=== AUDIT LOG ===");
		logger.info("Action: {} | Entity: {} | EntityID: {}", event.getActionType(),
				event.getEntityType(), event.getEntityId());
		logger.info("User: {} ({} - {})", event.getUsername(), event.getUserId(),
				event.getUserRole());
		logger.info("Description: {}", event.getDescription());
		logger.info("IP: {} | Method: {} | URL: {}", event.getIpAddress(),
				event.getRequestMethod(), event.getRequestUrl());
		logger.info("Status: {}", event.getStatus());
		if (event.getErrorMessage() != null) {
			logger.info("Error: {}", event.getErrorMessage());
		}
		logger.info("=================");

		// TODO: Save to database or external logging service
		// Example:
		// AuditLog auditLog = new AuditLog();
		// auditLog.setActionType(event.getActionType());
		// auditLog.setEntityType(event.getEntityType());
		// ... set other fields
		// auditLogRepository.save(auditLog);
	}
}

