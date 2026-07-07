package ar.edu.utn.frba.ddsi.climalert.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRecord {

    private String city;
    private String country;

    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private Double windKph;
    private String windDir;
    private Double pressureMb;
    private Double precipMm;
    private Integer cloud;
    private Double uv;
    private Double visKm;

    private String conditionText;
    private String conditionIcon;

    private LocalDateTime recordedAt;
}
