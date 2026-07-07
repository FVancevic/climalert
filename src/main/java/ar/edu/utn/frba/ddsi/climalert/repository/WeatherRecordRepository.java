package ar.edu.utn.frba.ddsi.climalert.repository;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class WeatherRecordRepository {

    private final List<WeatherRecord> records = new ArrayList<>();

    public void save(WeatherRecord record) {
        records.add(record);
    }

    public Optional<WeatherRecord> findTopByOrderByRecordedAtDesc() {
        return records.stream()
                .max(Comparator.comparing(WeatherRecord::getRecordedAt));
    }
}
