package com.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event untuk Payment Operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
	
	/**
	 * Tipe event: PROCESSED, FAILED, REFUNDED
	 */
	private String eventType;
	
	/**
	 * ID Health History
	 */
	private Integer healthHistoryId;
	
	/**
	 * ID Patient
	 */
	private Integer patientId;
	
	/**
	 * Nama Patient
	 */
	private String patientName;
	
	/**
	 * Email Patient
	 */
	private String patientEmail;
	
	/**
	 * Amount yang dibayar
	 */
	private Double amount;
	
	/**
	 * Total amount
	 */
	private Double totalAmount;
	
	/**
	 * Payment status
	 */
	private Boolean paymentStatus;
	
	/**
	 * Payment method
	 */
	private String paymentMethod;
	
	/**
	 * Transaction ID (jika ada)
	 */
	private String transactionId;
	
	/**
	 * Timestamp
	 */
	private Long timestamp;
}

