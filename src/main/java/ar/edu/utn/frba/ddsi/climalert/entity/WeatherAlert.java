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
public class WeatherAlert {

    private WeatherRecord weatherRecord;

    private Double temperature;
    private Integer humidity;
    private LocalDateTime alertedAt;
    private Boolean emailsSent;
}
