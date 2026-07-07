package ar.edu.utn.frba.ddsi.climalert.scheduler;

import ar.edu.utn.frba.ddsi.climalert.service.AlertService;
import ar.edu.utn.frba.ddsi.climalert.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherService weatherService;
    private final AlertService alertService;

    /**
     * Cada 5 minutos obtiene los datos climáticos actuales desde WeatherAPI
     * y los almacena localmente para registro histórico.
     */
    @Scheduled(fixedRateString = "${climalert.scheduler.fetch-rate-ms:300000}")
    public void fetchWeatherData() {
        log.info("=== [SCHEDULER] Iniciando fetch de datos climáticos ===");
        weatherService.fetchAndStoreWeatherData();
    }

    /**
     * Cada 1 minuto analiza la última información disponible del clima
     * y genera alertas si se detectan condiciones críticas.
     */
    @Scheduled(fixedRateString = "${climalert.scheduler.alert-rate-ms:60000}")
    public void processAlerts() {
        log.info("=== [SCHEDULER] Iniciando procesamiento de alertas ===");
        alertService.processLatestWeatherData();
    }
}
