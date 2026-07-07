package ar.edu.utn.frba.ddsi.climalert.service;

import ar.edu.utn.frba.ddsi.climalert.entity.WeatherRecord;

public interface EmailService {

    void sendAlertEmail(WeatherRecord record);
}
