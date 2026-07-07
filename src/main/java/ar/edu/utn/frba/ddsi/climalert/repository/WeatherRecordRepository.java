package ar.edu.utn.frba.ddsi.climalert.repository;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    Optional<WeatherRecord> findTopByOrderByRecordedAtDesc();
}
