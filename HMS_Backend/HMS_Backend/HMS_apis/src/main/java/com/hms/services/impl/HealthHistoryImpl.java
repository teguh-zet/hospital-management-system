package com.hms.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hms.entities.Doctor;
import com.hms.entities.Health_History;
import com.hms.entities.Medicine;
import com.hms.entities.Patient;
import com.hms.entities.User;
import com.hms.entities.Ward;
import com.hms.events.AppointmentEvent;
import com.hms.events.AuditEvent;
import com.hms.events.PaymentEvent;
import com.hms.events.ResourceEvent;
import com.hms.exceptions.ResourceNotFoundException;
import com.hms.payloads.HealthHistoryDto;
import com.hms.repository.HealthHistoryRepo;
import com.hms.repository.PatientRepo;
import com.hms.repository.WardRepo;
import com.hms.services.HealthHistoryService;
import com.hms.services.RabbitMQProducerService;

@Service
public class HealthHistoryImpl implements HealthHistoryService {

	@Autowired
	private HealthHistoryRepo healthRepo;

	@Autowired
	private PatientRepo patientRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WardRepo wardRepo;

	@Autowired
	private RabbitMQProducerService rabbitMQProducerService;

	// add patient appointment (create health history )
	@Override
	public HealthHistoryDto addAppointment(HealthHistoryDto healthDto, Integer patientId) {
		Patient patient = this.patientRepo.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient ", "Patient id", patientId));

		Health_History healths = this.modelMapper.map(healthDto, Health_History.class);

		patient.setCurrentStatus(true);
		Patient updatedPatient = this.patientRepo.save(patient);
		healths.setPatient(updatedPatient);
		healths.setPaymentStatus(true);
		healths.setMedicines(new ArrayList<Medicine>());
		Health_History newHealths = this.healthRepo.save(healths);

		// Publish Appointment Created Event ke RabbitMQ
		publishAppointmentCreatedEvent(newHealths, patient);

		// Publish Audit Event
		publishAuditEvent("CREATE", "APPOINTMENT", String.valueOf(newHealths.getId()),
				"Appointment created for patient: " + patient.getUser().getFirstName() + " "
						+ patient.getUser().getLastName());

		return this.modelMapper.map(newHealths, HealthHistoryDto.class);
	}

	/**
	 * Helper method untuk publish Appointment Created Event
	 */
	private void publishAppointmentCreatedEvent(Health_History healthHistory, Patient patient) {
		AppointmentEvent event = new AppointmentEvent();
		event.setEventType("CREATED");
		event.setHealthHistoryId(healthHistory.getId());
		event.setPatientId(patient.getId());
		event.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
		event.setPatientEmail(patient.getUser().getEmail());
		event.setAppointmentDate(healthHistory.getAppointmentDate());
		event.setAppointmentTime(healthHistory.getAppointmentTime());
		event.setSymptoms(healthHistory.getSymptoms());
		event.setPaymentStatus(healthHistory.getPaymentStatus());
		event.setTimestamp(System.currentTimeMillis());

		// Jika ada doctor yang assigned
		if (patient.getDoctor() != null) {
			Doctor doctor = patient.getDoctor();
			event.setDoctorId(doctor.getId());
			if (doctor.getEmployee() != null && doctor.getEmployee().getUser() != null) {
				User doctorUser = doctor.getEmployee().getUser();
				event.setDoctorName(doctorUser.getFirstName() + " " + doctorUser.getLastName());
				event.setDoctorEmail(doctorUser.getEmail());
			}
		}

		rabbitMQProducerService.publishAppointmentEvent(event);
	}

	// ---------------------------------------------------------------------------------------------------------

	@SuppressWarnings("null")
	@Override
	public List<HealthHistoryDto> getAppointmentHistoryBypatient(Integer patientId) {
		Patient patient = this.patientRepo.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", patientId));

		List<Health_History> healths = this.healthRepo.findByPatient(patient);

		List<Health_History> temp = new ArrayList<Health_History>();

		for (Health_History h : healths) {
			if (patient.getAdmitStatus().equals(false) && patient.getCurrentStatus().equals(true)
					&& h.getPaymentStatus().equals(true)) {
				temp.add(h);
			}
		}

		List<HealthHistoryDto> healthDtos = temp.stream()
				.map((health) -> this.modelMapper.map(health, HealthHistoryDto.class)).collect(Collectors.toList());

		return healthDtos;
	}

	// -------------------------------------------------------------------------------------------------------------

	// get health history by patient
	@SuppressWarnings("null")
	@Override
	public List<HealthHistoryDto> getHealthHistoryBypatient(Integer patientId) {
		Patient patient = this.patientRepo.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", patientId));
		List<Health_History> healths = this.healthRepo.findByPatient(patient);

		List<Health_History> temp = new ArrayList<Health_History>();

		for (Health_History h : healths) {
			if (h.getPaymentStatus().equals(false)) {
				temp.add(h);
			}
		}

		List<HealthHistoryDto> healthDtos = temp.stream()
				.map((health) -> this.modelMapper.map(health, HealthHistoryDto.class)).collect(Collectors.toList());

		return healthDtos;
	}

	// ---------------------------------------------------------------------------------------------------------------

	@Override
	public void deleteHealthHistory(Integer healthId) {
		Health_History health = this.healthRepo.findById(healthId)
				.orElseThrow(() -> new ResourceNotFoundException("Health_History ", "health id", healthId));

		Patient patient = health.getPatient();

		// Publish Appointment Cancelled Event
		AppointmentEvent event = new AppointmentEvent();
		event.setEventType("CANCELLED");
		event.setHealthHistoryId(healthId);
		event.setPatientId(patient.getId());
		event.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
		event.setPatientEmail(patient.getUser().getEmail());
		event.setAppointmentDate(health.getAppointmentDate());
		event.setAppointmentTime(health.getAppointmentTime());
		event.setTimestamp(System.currentTimeMillis());
		rabbitMQProducerService.publishAppointmentEvent(event);

		// Publish Audit Event
		publishAuditEvent("DELETE", "APPOINTMENT", String.valueOf(healthId),
				"Appointment cancelled for patient: " + patient.getUser().getFirstName() + " "
						+ patient.getUser().getLastName());

		this.healthRepo.delete(health);
	}

	// ---------------------------------------------------------------------------------------------------------------------

	@Override
	public HealthHistoryDto getHealthHistoryById(Integer healthId) {
		Health_History healths = this.healthRepo.findById(healthId)
				.orElseThrow(() -> new ResourceNotFoundException("Health_History", "health id", healthId));
		return this.modelMapper.map(healths, HealthHistoryDto.class);
	}

	// ----------------------------------------------------------------------------------------------------------------------

	@Override
	public HealthHistoryDto updatePatientWard(HealthHistoryDto healthDto, Integer wardId) {
		Health_History hh = this.modelMapper.map(healthDto, Health_History.class);

		Health_History healths = this.healthRepo.findById(hh.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Health_History", "health id", hh.getId()));

		Patient patient = healths.getPatient();

		Ward ward = this.wardRepo.findById(wardId)
				.orElseThrow(() -> new ResourceNotFoundException("Ward", "ward id ", wardId));

		patient.setWard(ward);
		healths.setAllocatedBed(healthDto.getAllocatedBed());

		@SuppressWarnings("unused")
		Patient updatedPatient = this.patientRepo.save(patient);

		healths.setAdmitDate(LocalDate.now());

		Health_History updatedHealth = this.healthRepo.save(healths);

		// Publish Appointment Admitted Event
		publishAppointmentAdmittedEvent(updatedHealth, patient, ward);

		// Publish Resource Event (Bed Allocated)
		ResourceEvent resourceEvent = new ResourceEvent();
		resourceEvent.setEventType("BED_ALLOCATED");
		resourceEvent.setResourceType("BED");
		resourceEvent.setResourceId(healthDto.getAllocatedBed());
		resourceEvent.setWardId(wardId);
		resourceEvent.setBedNumber(healthDto.getAllocatedBed());
		resourceEvent.setStatus("OCCUPIED");
		resourceEvent.setPatientId(patient.getId());
		resourceEvent.setHealthHistoryId(updatedHealth.getId());
		resourceEvent.setTimestamp(System.currentTimeMillis());
		rabbitMQProducerService.publishResourceEvent(resourceEvent);

		// Publish Audit Event
		publishAuditEvent("UPDATE", "APPOINTMENT", String.valueOf(updatedHealth.getId()),
				"Patient admitted to ward: " + wardId + ", bed: " + healthDto.getAllocatedBed());

		return this.modelMapper.map(updatedHealth, HealthHistoryDto.class);
	}

	/**
	 * Helper method untuk publish Appointment Admitted Event
	 */
	private void publishAppointmentAdmittedEvent(Health_History healthHistory, Patient patient, Ward ward) {
		AppointmentEvent event = new AppointmentEvent();
		event.setEventType("ADMITTED");
		event.setHealthHistoryId(healthHistory.getId());
		event.setPatientId(patient.getId());
		event.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
		event.setPatientEmail(patient.getUser().getEmail());
		event.setAppointmentDate(healthHistory.getAppointmentDate());
		event.setAppointmentTime(healthHistory.getAppointmentTime());
		event.setWardId(ward.getId());
		event.setAllocatedBed(healthHistory.getAllocatedBed());
		event.setPaymentStatus(healthHistory.getPaymentStatus());
		event.setTimestamp(System.currentTimeMillis());

		if (patient.getDoctor() != null) {
			Doctor doctor = patient.getDoctor();
			event.setDoctorId(doctor.getId());
			if (doctor.getEmployee() != null && doctor.getEmployee().getUser() != null) {
				User doctorUser = doctor.getEmployee().getUser();
				event.setDoctorName(doctorUser.getFirstName() + " " + doctorUser.getLastName());
				event.setDoctorEmail(doctorUser.getEmail());
			}
		}

		rabbitMQProducerService.publishAppointmentEvent(event);
	}

	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public HealthHistoryDto dischargePatient(Integer healthId) {
		Health_History healths = this.healthRepo.findById(healthId)
				.orElseThrow(() -> new ResourceNotFoundException("HealthHistory ", "health id", healthId));

		Patient patient = healths.getPatient();
		Ward ward = patient.getWard();
		String allocatedBed = healths.getAllocatedBed();

		patient.setAdmitStatus(false);
		patient.setCurrentStatus(false);
		patient.setWard(null);
		patient.setDoctor(null);

		@SuppressWarnings("unused")
		Patient updatedPatient = this.patientRepo.save(patient);

		healths.setPaymentStatus(false);
		healths.setDischargeDate(LocalDate.now());

		Health_History updatedHealth = this.healthRepo.save(healths);

		// Publish Appointment Discharged Event
		publishAppointmentDischargedEvent(updatedHealth, patient);

		// Publish Resource Event (Bed Released)
		if (ward != null && allocatedBed != null) {
			ResourceEvent resourceEvent = new ResourceEvent();
			resourceEvent.setEventType("BED_RELEASED");
			resourceEvent.setResourceType("BED");
			resourceEvent.setResourceId(allocatedBed);
			resourceEvent.setWardId(ward.getId());
			resourceEvent.setBedNumber(allocatedBed);
			resourceEvent.setStatus("AVAILABLE");
			resourceEvent.setPatientId(patient.getId());
			resourceEvent.setHealthHistoryId(updatedHealth.getId());
			resourceEvent.setTimestamp(System.currentTimeMillis());
			rabbitMQProducerService.publishResourceEvent(resourceEvent);
		}

		// Publish Audit Event
		publishAuditEvent("UPDATE", "APPOINTMENT", String.valueOf(updatedHealth.getId()),
				"Patient discharged: " + patient.getUser().getFirstName() + " "
						+ patient.getUser().getLastName());

		return this.modelMapper.map(updatedHealth, HealthHistoryDto.class);
	}

	/**
	 * Helper method untuk publish Appointment Discharged Event
	 */
	private void publishAppointmentDischargedEvent(Health_History healthHistory, Patient patient) {
		AppointmentEvent event = new AppointmentEvent();
		event.setEventType("DISCHARGED");
		event.setHealthHistoryId(healthHistory.getId());
		event.setPatientId(patient.getId());
		event.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
		event.setPatientEmail(patient.getUser().getEmail());
		event.setAppointmentDate(healthHistory.getAppointmentDate());
		event.setDischargeDate(healthHistory.getDischargeDate());
		event.setPaymentStatus(healthHistory.getPaymentStatus());
		event.setTimestamp(System.currentTimeMillis());

		rabbitMQProducerService.publishAppointmentEvent(event);
	}

	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public HealthHistoryDto updateHealthHistoryPayment(Integer Id, Double amt) {
		Health_History healths = this.healthRepo.findById(Id)
				.orElseThrow(() -> new ResourceNotFoundException("HealthHistory ", "health id", Id));
		double a = healths.getPaidAmount() + amt;
		healths.setPaidAmount(a);
		healths.setPaymentDate(LocalDate.now());

		Health_History updatedHealth = this.healthRepo.save(healths);

		// Publish Payment Event
		Patient patient = updatedHealth.getPatient();
		PaymentEvent paymentEvent = new PaymentEvent();
		paymentEvent.setEventType("PROCESSED");
		paymentEvent.setHealthHistoryId(Id);
		paymentEvent.setPatientId(patient.getId());
		paymentEvent.setPatientName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
		paymentEvent.setPatientEmail(patient.getUser().getEmail());
		paymentEvent.setAmount(amt);
		paymentEvent.setTotalAmount(updatedHealth.getPaidAmount());
		paymentEvent.setPaymentStatus(updatedHealth.getPaymentStatus());
		paymentEvent.setPaymentMethod("CASH"); // Default, bisa di-extend
		paymentEvent.setTimestamp(System.currentTimeMillis());
		rabbitMQProducerService.publishPaymentEvent(paymentEvent);

		// Publish Audit Event
		publishAuditEvent("UPDATE", "PAYMENT", String.valueOf(Id),
				"Payment processed: " + amt + " for patient: " + patient.getUser().getFirstName() + " "
						+ patient.getUser().getLastName());

		return this.modelMapper.map(updatedHealth, HealthHistoryDto.class);
	}

	// --------------------------------------------------------------------------------------------------------------------

	// update HH (Doctor)
	@Override
	public HealthHistoryDto updateHealthHistory(HealthHistoryDto healthDto, Boolean admitStatus) {

		Health_History hh = this.modelMapper.map(healthDto, Health_History.class);

		Health_History healths = this.healthRepo.findById(hh.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Health_History", "health id", hh.getId()));

		Patient pat = healths.getPatient();
		pat.setAdmitStatus(admitStatus);

		@SuppressWarnings("unused")
		Patient updatedPatient = this.patientRepo.save(pat);

		healths.setDiseases(healthDto.getDiseases());
		healths.setPrescriptionInstruction(healthDto.getPrescriptionInstruction());
		healths.setMedicines(new ArrayList<Medicine>());

		Health_History updatedHealth = this.healthRepo.save(healths);
		return this.modelMapper.map(updatedHealth, HealthHistoryDto.class);
	}

	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public HealthHistoryDto getHealthHistoryByPaymentStatus(Integer patientId) {
		Patient patient = this.patientRepo.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", patientId));
		List<Health_History> healths = this.healthRepo.findByPatient(patient);
		List<HealthHistoryDto> healthDtos = healths.stream()
				.map((health) -> this.modelMapper.map(health, HealthHistoryDto.class)).collect(Collectors.toList());
		HealthHistoryDto h = null;
		for (HealthHistoryDto hdo : healthDtos) {
			if (hdo.getPaymentStatus().equals(true)) {
				h = hdo;
			}
		}
		return h;
	}

	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * Helper method untuk publish Audit Event
	 */
	private void publishAuditEvent(String actionType, String entityType, String entityId, String description) {
		AuditEvent auditEvent = new AuditEvent();
		auditEvent.setActionType(actionType);
		auditEvent.setEntityType(entityType);
		auditEvent.setEntityId(entityId);
		auditEvent.setDescription(description);
		auditEvent.setStatus("SUCCESS");
		auditEvent.setTimestamp(System.currentTimeMillis());

		// Get current user from security context
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.getPrincipal() instanceof User) {
				User currentUser = (User) authentication.getPrincipal();
				auditEvent.setUserId(currentUser.getId());
				auditEvent.setUsername(currentUser.getEmail());
				if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
					auditEvent.setUserRole(currentUser.getRoles().iterator().next().getName());
				}
			}
		} catch (Exception e) {
			// If unable to get user, continue without user info
		}

		rabbitMQProducerService.publishAuditEvent(auditEvent);
	}
}