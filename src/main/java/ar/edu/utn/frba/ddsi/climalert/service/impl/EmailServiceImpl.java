package ar.edu.utn.frba.ddsi.climalert.service.impl;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import ar.edu.utn.frba.ddsi.climalert.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject(buildSubject(record));
        message.setText(buildBody(record));

        mailSender.send(message);
        log.info("Correos de alerta enviados a: {}", recipients);
    }

    private String buildSubject(WeatherRecord record) {
        return String.format("[ALERTA CLIMÁTICA] Condiciones críticas en %s - Temp: %.1f°C | Humedad: %d%%",
                record.getCity(), record.getTemperature(), record.getHumidity());
    }

    private String buildBody(WeatherRecord record) {
        return """
                ALERTA CLIMÁTICA - CLIMALERT
                ==============================
                Se detectaron condiciones críticas (temp > 35°C y humedad > 60%%).

                Ubicación : %s, %s
                Fecha/hora: %s

                --- Condiciones actuales ---
                Condición      : %s
                Temperatura    : %.1f°C (sensación: %.1f°C)
                Humedad        : %d%%
                Viento         : %.1f km/h %s
                Presión        : %.1f hPa
                Precipitaciones: %.1f mm
                Nubosidad      : %d%%
                Visibilidad    : %.1f km
                Índice UV      : %.1f

                Este es un correo automático de Climalert - UTN FRBA DDS.
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
