package ar.edu.utn.frba.ddsi.climalert.service.impl;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import ar.edu.utn.frba.ddsi.climalert.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${climalert.email.from}")
    private String fromEmail;

    @Value("${climalert.email.recipients}")
    private List<String> recipients;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void sendAlertEmail(WeatherRecord record) {
        String subject = buildSubject(record);
        String body = buildEmailBody(record);

        for (String recipient : recipients) {
            try {
                sendEmail(recipient, subject, body);
                log.info("Correo de alerta enviado a: {}", recipient);
            } catch (MessagingException e) {
                log.error("Error al enviar correo a {}: {}", recipient, e.getMessage(), e);
            }
        }
    }

    private void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    private String buildSubject(WeatherRecord record) {
        return String.format("[ALERTA CLIMÁTICA] Condiciones críticas en %s - Temp: %.1f°C | Humedad: %d%%",
                record.getCity(), record.getTemperature(), record.getHumidity());
    }

    private String buildEmailBody(WeatherRecord record) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #c0392b; color: white; padding: 20px; border-radius: 8px 8px 0 0; text-align: center; }
                        .header h1 { margin: 0; font-size: 24px; }
                        .content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }
                        .alert-box { background-color: #fdecea; border: 2px solid #c0392b; border-radius: 6px; padding: 15px; margin-bottom: 20px; }
                        .data-table { width: 100%%; border-collapse: collapse; margin-top: 15px; }
                        .data-table th { background-color: #2c3e50; color: white; padding: 10px; text-align: left; }
                        .data-table td { padding: 10px; border-bottom: 1px solid #ddd; }
                        .data-table tr:nth-child(even) { background-color: #ecf0f1; }
                        .critical { color: #c0392b; font-weight: bold; }
                        .footer { background-color: #2c3e50; color: #aaa; padding: 15px; text-align: center; font-size: 12px; border-radius: 0 0 8px 8px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>⚠ ALERTA CLIMÁTICA - CLIMALERT</h1>
                        </div>
                        <div class="content">
                            <div class="alert-box">
                                <strong>Se han detectado condiciones climáticas críticas:</strong>
                                <ul>
                                    <li>Temperatura superior a 35°C</li>
                                    <li>Humedad superior a 60%%</li>
                                </ul>
                            </div>

                            <h3>Detalle completo del clima</h3>
                            <table class="data-table">
                                <tr><th colspan="2">Información de ubicación</th></tr>
                                <tr><td>Ciudad</td><td>%s, %s</td></tr>
                                <tr><td>Fecha y hora del registro</td><td>%s</td></tr>

                                <tr><th colspan="2">Condiciones actuales</th></tr>
                                <tr><td>Condición</td><td>%s</td></tr>
                                <tr><td>Temperatura</td><td class="critical">%.1f°C</td></tr>
                                <tr><td>Sensación térmica</td><td>%.1f°C</td></tr>
                                <tr><td>Humedad</td><td class="critical">%d%%</td></tr>
                                <tr><td>Viento</td><td>%.1f km/h %s</td></tr>
                                <tr><td>Presión</td><td>%.1f hPa</td></tr>
                                <tr><td>Precipitaciones</td><td>%.1f mm</td></tr>
                                <tr><td>Nubosidad</td><td>%d%%</td></tr>
                                <tr><td>Visibilidad</td><td>%.1f km</td></tr>
                                <tr><td>Índice UV</td><td>%.1f</td></tr>
                            </table>
                        </div>
                        <div class="footer">
                            <p>Climalert - Sistema de monitoreo climático automático | UTN FRBA - DDS</p>
                            <p>Este es un correo automático, por favor no responder.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                record.getCity(), record.getCountry(),
                record.getRecordedAt() != null ? record.getRecordedAt().format(FORMATTER) : "N/A",
                record.getConditionText() != null ? record.getConditionText() : "N/A",
                record.getTemperature(),
                record.getFeelsLike() != null ? record.getFeelsLike() : 0.0,
                record.getHumidity(),
                record.getWindKph() != null ? record.getWindKph() : 0.0,
                record.getWindDir() != null ? record.getWindDir() : "",
                record.getPressureMb() != null ? record.getPressureMb() : 0.0,
                record.getPrecipMm() != null ? record.getPrecipMm() : 0.0,
                record.getCloud() != null ? record.getCloud() : 0,
                record.getVisKm() != null ? record.getVisKm() : 0.0,
                record.getUv() != null ? record.getUv() : 0.0
        );
    }
}
