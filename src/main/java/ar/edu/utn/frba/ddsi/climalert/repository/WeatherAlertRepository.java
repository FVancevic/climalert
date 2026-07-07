package ar.edu.utn.frba.ddsi.climalert.repository;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, Long> {

    Optional<WeatherAlert> findTopByOrderByAlertedAtDesc();

    boolean existsByAlertedAtAfter(LocalDateTime since);
}
