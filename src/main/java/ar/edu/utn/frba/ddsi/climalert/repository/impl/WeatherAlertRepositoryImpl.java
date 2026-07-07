package ar.edu.utn.frba.ddsi.climalert.repository.impl;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherAlert;
import ar.edu.utn.frba.ddsi.climalert.repository.WeatherAlertRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class WeatherAlertRepositoryImpl implements WeatherAlertRepository {

    private final List<WeatherAlert> alerts = new ArrayList<>();

    @Override
    public void save(WeatherAlert alert) {
        alerts.add(alert);
    }

    @Override
    public Optional<WeatherAlert> findTopByOrderByAlertedAtDesc() {
        return alerts.stream()
                .max(Comparator.comparing(WeatherAlert::getAlertedAt));
    }

    @Override
    public boolean existsByAlertedAtAfter(LocalDateTime since) {
        return alerts.stream()
                .anyMatch(a -> a.getAlertedAt().isAfter(since));
    }
}
