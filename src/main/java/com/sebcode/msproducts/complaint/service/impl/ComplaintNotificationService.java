package com.sebcode.msproducts.complaint.service.impl;

import com.sebcode.msproducts.complaint.entity.Complaint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintNotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String from;

    @Value("${app.complaints.notification-email:}")
    private String notificationEmail;

    /**
     * Nunca debe romper el flujo de creación del reclamo: cualquier fallo de
     * envío (SMTP no configurado, credenciales inválidas, timeout) se registra
     * y se devuelve false, sin propagar la excepción.
     */
    public boolean notify(Complaint complaint) {
        if (notificationEmail == null || notificationEmail.isBlank()) {
            log.warn("COMPLAINT_NOTIFICATION_EMAIL no configurado; se omite notificación del reclamo #{}", complaint.getId());
            return false;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (from != null && !from.isBlank()) {
                message.setFrom(from);
            }
            message.setTo(notificationEmail);
            message.setSubject("Nuevo " + complaint.getType() + " #" + complaint.getId() + " — Libro de Reclamaciones");
            message.setText(buildBody(complaint));
            mailSender.send(message);
            return true;
        } catch (Exception ex) {
            log.error("No se pudo enviar la notificación por email del reclamo #{}: {}", complaint.getId(), ex.getMessage());
            return false;
        }
    }

    private String buildBody(Complaint complaint) {
        return """
                Se registró un nuevo %s en el Libro de Reclamaciones.

                Fecha: %s

                --- Datos del consumidor ---
                Nombre: %s %s
                Documento: %s %s
                Domicilio: %s
                Email: %s
                Teléfono: %s
                %s

                --- Datos del bien contratado ---
                Tipo: %s
                Monto reclamado: %s
                Descripción: %s

                --- Detalle ---
                %s

                --- Pedido concreto del consumidor ---
                %s
                """.formatted(
                complaint.getType(),
                complaint.getCreatedAt() != null ? complaint.getCreatedAt().format(FORMATTER) : "-",
                complaint.getConsumerName(), complaint.getConsumerLastName(),
                complaint.getDocumentType(), complaint.getDocumentNumber(),
                complaint.getAddress(),
                complaint.getEmail(),
                complaint.getPhone(),
                Boolean.TRUE.equals(complaint.getIsMinor())
                        ? "Es menor de edad — apoderado: " + complaint.getGuardianName()
                        : "",
                complaint.getProductType(),
                complaint.getAmountClaimed() != null ? complaint.getAmountClaimed().toString() : "No especificado",
                complaint.getProductDescription(),
                complaint.getDetail(),
                complaint.getConsumerRequest()
        );
    }

}
