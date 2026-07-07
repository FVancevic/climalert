package ar.edu.utn.frba.ddsi.climalert.client.impl;

import ar.edu.utn.frba.ddsi.climalert.client.WeatherApiClient;
import ar.edu.utn.frba.ddsi.climalert.dto.WeatherApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClientImpl implements WeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${weatherapi.base-url}")
    private String baseUrl;

    @Value("${weatherapi.api-key}")
    private String apiKey;

    @Override
    public WeatherApiResponseDto getCurrentWeather(String location) {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", location)
                .queryParam("aqi", "no")
                .toUriString();

        log.info("Consultando WeatherAPI para la ubicación: {}", location);
        WeatherApiResponseDto response = restTemplate.getForObject(url, WeatherApiResponseDto.class);
        log.info("Respuesta recibida de WeatherAPI: temp={}°C, humedad={}%",
                response != null && response.getCurrent() != null ? response.getCurrent().getTempC() : "N/A",
                response != null && response.getCurrent() != null ? response.getCurrent().getHumidity() : "N/A");

        return response;
    }
}
