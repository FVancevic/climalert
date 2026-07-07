package ar.edu.utn.frba.ddsi.climalert.repository.impl;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;
import ar.edu.utn.frba.ddsi.climalert.repository.WeatherRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class WeatherRecordRepositoryImpl implements WeatherRecordRepository {

    private final List<WeatherRecord> records = new ArrayList<>();

    @Override
    public void save(WeatherRecord record) {
        records.add(record);
    }

    @Override
    public Optional<WeatherRecord> findTopByOrderByRecordedAtDesc() {
        return records.stream()
                .max(Comparator.comparing(WeatherRecord::getRecordedAt));
    }
}
