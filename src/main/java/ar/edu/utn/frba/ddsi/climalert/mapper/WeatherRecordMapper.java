package ar.edu.utn.frba.ddsi.climalert.mapper;

import ar.edu.utn.frba.ddsi.climalert.dto.CurrentDto;
import ar.edu.utn.frba.ddsi.climalert.dto.LocationDto;
import ar.edu.utn.frba.ddsi.climalert.dto.WeatherApiResponseDto;
import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WeatherRecordMapper {

    public WeatherRecord toWeatherRecord(WeatherApiResponseDto response) {
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
