package ar.edu.utn.frba.ddsi.climalert.repository;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;

import java.util.Optional;

public interface WeatherRecordRepository {

    void save(WeatherRecord record);

    Optional<WeatherRecord> findTopByOrderByRecordedAtDesc();
}
