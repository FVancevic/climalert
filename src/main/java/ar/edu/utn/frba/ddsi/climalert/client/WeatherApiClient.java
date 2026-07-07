package ar.edu.utn.frba.ddsi.climalert.client;

import ar.edu.utn.frba.ddsi.climalert.dto.WeatherApiResponseDto;

public interface WeatherApiClient {

    WeatherApiResponseDto getCurrentWeather(String location);
}
