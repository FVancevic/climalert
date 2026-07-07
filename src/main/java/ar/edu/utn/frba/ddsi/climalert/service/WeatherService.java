package ar.edu.utn.frba.ddsi.climalert.service;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;

import java.util.Optional;

public interface WeatherService {

    void fetchAndStoreWeatherData();

    Optional<WeatherRecord> getLatestRecord();
}
