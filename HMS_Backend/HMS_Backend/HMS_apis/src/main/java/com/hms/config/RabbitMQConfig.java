package com.hms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurasi RabbitMQ untuk Hospital Management System
 * 
 * Struktur Messaging:
 * - Exchange: hms.exchange (Direct Exchange)
 * - Queues:
 *   1. hms.appointment.queue - Untuk event appointment (created, updated, cancelled)
 *   2. hms.notification.queue - Untuk notifikasi (email, SMS)
 *   3. hms.payment.queue - Untuk event pembayaran
 *   4. hms.resource.queue - Untuk update resource availability
 *   5. hms.audit.queue - Untuk audit logging
 */
@Configuration
public class RabbitMQConfig {

	// Exchange
	public static final String HMS_EXCHANGE = "hms.exchange";

	// Queue Names
	public static final String APPOINTMENT_QUEUE = "hms.appointment.queue";
	public static final String NOTIFICATION_QUEUE = "hms.notification.queue";
	public static final String PAYMENT_QUEUE = "hms.payment.queue";
	public static final String RESOURCE_QUEUE = "hms.resource.queue";
	public static final String AUDIT_QUEUE = "hms.audit.queue";

	// Routing Keys
	public static final String APPOINTMENT_ROUTING_KEY = "hms.appointment";
	public static final String NOTIFICATION_ROUTING_KEY = "hms.notification";
	public static final String PAYMENT_ROUTING_KEY = "hms.payment";
	public static final String RESOURCE_ROUTING_KEY = "hms.resource";
	public static final String AUDIT_ROUTING_KEY = "hms.audit";

	/**
	 * Membuat Direct Exchange untuk HMS
	 */
	@Bean
	public DirectExchange hmsExchange() {
		return new DirectExchange(HMS_EXCHANGE, true, false);
	}

	/**
	 * Queue untuk Appointment Events
	 */
	@Bean
	public Queue appointmentQueue() {
		return new Queue(APPOINTMENT_QUEUE, true, false, false);
	}

	/**
	 * Queue untuk Notification Events
	 */
	@Bean
	public Queue notificationQueue() {
		return new Queue(NOTIFICATION_QUEUE, true, false, false);
	}

	/**
	 * Queue untuk Payment Events
	 */
	@Bean
	public Queue paymentQueue() {
		return new Queue(PAYMENT_QUEUE, true, false, false);
	}

	/**
	 * Queue untuk Resource Update Events
	 */
	@Bean
	public Queue resourceQueue() {
		return new Queue(RESOURCE_QUEUE, true, false, false);
	}

	/**
	 * Queue untuk Audit Log Events
	 */
	@Bean
	public Queue auditQueue() {
		return new Queue(AUDIT_QUEUE, true, false, false);
	}

	/**
	 * Binding Appointment Queue ke Exchange
	 */
	@Bean
	public Binding appointmentBinding() {
		return BindingBuilder.bind(appointmentQueue()).to(hmsExchange())
				.with(APPOINTMENT_ROUTING_KEY);
	}

	/**
	 * Binding Notification Queue ke Exchange
	 */
	@Bean
	public Binding notificationBinding() {
		return BindingBuilder.bind(notificationQueue()).to(hmsExchange())
				.with(NOTIFICATION_ROUTING_KEY);
	}

	/**
	 * Binding Payment Queue ke Exchange
	 */
	@Bean
	public Binding paymentBinding() {
		return BindingBuilder.bind(paymentQueue()).to(hmsExchange()).with(PAYMENT_ROUTING_KEY);
	}

	/**
	 * Binding Resource Queue ke Exchange
	 */
	@Bean
	public Binding resourceBinding() {
		return BindingBuilder.bind(resourceQueue()).to(hmsExchange()).with(RESOURCE_ROUTING_KEY);
	}

	/**
	 * Binding Audit Queue ke Exchange
	 */
	@Bean
	public Binding auditBinding() {
		return BindingBuilder.bind(auditQueue()).to(hmsExchange()).with(AUDIT_ROUTING_KEY);
	}

	/**
	 * Message Converter untuk JSON
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * RabbitTemplate dengan JSON Message Converter
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}
}

