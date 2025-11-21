package com.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event untuk Resource Updates (Ward, Bed, Equipment)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceEvent {
	
	/**
	 * Tipe event: BED_ALLOCATED, BED_RELEASED, WARD_UPDATED, EQUIPMENT_UPDATED
	 */
	private String eventType;
	
	/**
	 * Resource type: WARD, BED, EQUIPMENT
	 */
	private String resourceType;
	
	/**
	 * Resource ID
	 */
	private String resourceId;
	
	/**
	 * Ward ID (jika applicable)
	 */
	private Integer wardId;
	
	/**
	 * Bed number (jika applicable)
	 */
	private String bedNumber;
	
	/**
	 * Status: AVAILABLE, OCCUPIED, MAINTENANCE
	 */
	private String status;
	
	/**
	 * Patient ID (jika bed di-allocate)
	 */
	private Integer patientId;
	
	/**
	 * Health History ID
	 */
	private Integer healthHistoryId;
	
	/**
	 * Timestamp
	 */
	private Long timestamp;
}

