package ar.edu.utn.frba.ddsi.climalert.service.impl;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherAlert;
import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import ar.edu.utn.frba.ddsi.climalert.repository.WeatherAlertRepository;
import ar.edu.utn.frba.ddsi.climalert.service.AlertService;
import ar.edu.utn.frba.ddsi.climalert.service.EmailService;
import ar.edu.utn.frba.ddsi.climalert.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private static final double TEMPERATURE_THRESHOLD = 35.0;
    private static final int HUMIDITY_THRESHOLD = 60;

    private final WeatherService weatherService;
    private final WeatherAlertRepository weatherAlertRepository;
    private final EmailService emailService;

    @Value("${climalert.alert.cooldown-minutes:5}")
    private int cooldownMinutes;

    @Override
    public void processLatestWeatherData() {
        Optional<WeatherRecord> latestRecord = weatherService.getLatestRecord();

        if (latestRecord.isEmpty()) {
            log.info("No hay registros climáticos disponibles para analizar");
            return;
        }

        WeatherRecord record = latestRecord.get();
        log.info("Analizando condiciones climáticas: temp={}°C, humedad={}%",
                record.getTemperature(), record.getHumidity());

        if (isCritical(record)) {
            log.warn("Condiciones críticas detectadas: temp={}°C > {}°C y humedad={}% > {}%",
                    record.getTemperature(), TEMPERATURE_THRESHOLD,
                    record.getHumidity(), HUMIDITY_THRESHOLD);
            handleCriticalCondition(record);
        } else {
            log.info("Condiciones normales. No se genera alerta.");
        }
    }

    private boolean isCritical(WeatherRecord record) {
        return record.getTemperature() != null
                && record.getTemperature() > TEMPERATURE_THRESHOLD
                && record.getHumidity() != null
                && record.getHumidity() > HUMIDITY_THRESHOLD;
    }

    private void handleCriticalCondition(WeatherRecord record) {
        LocalDateTime cooldownThreshold = LocalDateTime.now().minusMinutes(cooldownMinutes);
        boolean recentAlertExists = weatherAlertRepository.existsByAlertedAtAfter(cooldownThreshold);

        if (recentAlertExists) {
            log.info("Ya existe una alerta reciente (dentro de los últimos {} minutos). No se genera una nueva.", cooldownMinutes);
            return;
        }

        WeatherAlert alert = WeatherAlert.builder()
                .weatherRecord(record)
                .temperature(record.getTemperature())
                .humidity(record.getHumidity())
                .alertedAt(LocalDateTime.now())
                .emailsSent(false)
                .build();

        try {
            emailService.sendAlertEmail(record);
            alert.setEmailsSent(true);
            log.info("Correos de alerta enviados exitosamente");
        } catch (Exception e) {
            log.error("Error al enviar correos de alerta: {}", e.getMessage(), e);
        } finally {
            weatherAlertRepository.save(alert);
            log.info("Alerta registrada en la base de datos");
        }
    }
}
