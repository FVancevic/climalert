package ar.edu.utn.frba.ddsi.climalert.repository;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherAlert;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherAlertRepository {

    void save(WeatherAlert alert);

    Optional<WeatherAlert> findTopByOrderByAlertedAtDesc();

    boolean existsByAlertedAtAfter(LocalDateTime since);
}
