package com.hms.events;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event untuk Appointment Operations
 * Digunakan untuk publish event ketika appointment dibuat, diupdate, atau dibatalkan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent {
	
	/**
	 * Tipe event: CREATED, UPDATED, CANCELLED, ADMITTED, DISCHARGED
	 */
	private String eventType;
	
	/**
	 * ID dari Health History (Appointment)
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
	 * ID Doctor (jika ada)
	 */
	private Integer doctorId;
	
	/**
	 * Nama Doctor (jika ada)
	 */
	private String doctorName;
	
	/**
	 * Email Doctor (jika ada)
	 */
	private String doctorEmail;
	
	/**
	 * Tanggal Appointment
	 */
	private LocalDate appointmentDate;
	
	/**
	 * Waktu Appointment
	 */
	private LocalTime appointmentTime;
	
	/**
	 * Discharge Date (jika di-discharge)
	 */
	private LocalDate dischargeDate;
	
	/**
	 * Symptoms/Gejala
	 */
	private String symptoms;
	
	/**
	 * Ward ID (jika di-admit)
	 */
	private Integer wardId;
	
	/**
	 * Allocated Bed (jika di-admit)
	 */
	private String allocatedBed;
	
	/**
	 * Timestamp event
	 */
	private Long timestamp;
	
	/**
	 * Status payment
	 */
	private Boolean paymentStatus;
}

