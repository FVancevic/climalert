package ar.edu.utn.frba.ddsi.climalert.service.impl;

import ar.edu.utn.frba.ddsi.climalert.client.WeatherApiClient;
import ar.edu.utn.frba.ddsi.climalert.dto.CurrentDto;
import ar.edu.utn.frba.ddsi.climalert.dto.LocationDto;
import ar.edu.utn.frba.ddsi.climalert.dto.WeatherApiResponseDto;
import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import ar.edu.utn.frba.ddsi.climalert.repository.WeatherRecordRepository;
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
public class WeatherServiceImpl implements WeatherService {

    private final WeatherApiClient weatherApiClient;
    private final WeatherRecordRepository weatherRecordRepository;

    @Value("${climalert.location}")
    private String location;

    @Override
    public void fetchAndStoreWeatherData() {
        log.info("Iniciando obtención de datos climáticos para: {}", location);
        try {
            WeatherApiResponseDto response = weatherApiClient.getCurrentWeather(location);

            if (response == null || response.getCurrent() == null || response.getLocation() == null) {
                log.warn("La respuesta de WeatherAPI fue nula o incompleta");
                return;
            }

            WeatherRecord record = mapToWeatherRecord(response);
            weatherRecordRepository.save(record);
            log.info("Registro climático guardado: temp={}°C, humedad={}%",
                    record.getTemperature(), record.getHumidity());

        } catch (Exception e) {
            log.error("Error al obtener datos climáticos de WeatherAPI: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<WeatherRecord> getLatestRecord() {
        return weatherRecordRepository.findTopByOrderByRecordedAtDesc();
    }

    private WeatherRecord mapToWeatherRecord(WeatherApiResponseDto response) {
        LocationDto loc = response.getLocation();
        CurrentDto cur = response.getCurrent();

        return WeatherRecord.builder()
                .city(loc.getName())
                .country(loc.getCountry())
                .temperature(cur.getTempC())
                .feelsLike(cur.getFeelsLikeC())
                .humidity(cur.getHumidity())
                .windKph(cur.getWindKph())
                .windDir(cur.getWindDir())
                .pressureMb(cur.getPressureMb())
                .precipMm(cur.getPrecipMm())
                .cloud(cur.getCloud())
                .uv(cur.getUv())
                .visKm(cur.getVisKm())
                .conditionText(cur.getCondition() != null ? cur.getCondition().getText() : null)
                .conditionIcon(cur.getCondition() != null ? cur.getCondition().getIcon() : null)
                .recordedAt(LocalDateTime.now())
                .build();
    }
}
