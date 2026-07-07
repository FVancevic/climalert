package ar.edu.utn.frba.ddsi.climalert.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentDto {

    @JsonProperty("temp_c")
    private Double tempC;

    @JsonProperty("feelslike_c")
    private Double feelsLikeC;

    private Integer humidity;

    @JsonProperty("wind_kph")
    private Double windKph;

    @JsonProperty("wind_dir")
    private String windDir;

    @JsonProperty("pressure_mb")
    private Double pressureMb;

    @JsonProperty("precip_mm")
    private Double precipMm;

    private Integer cloud;

    @JsonProperty("uv")
    private Double uv;

    @JsonProperty("vis_km")
    private Double visKm;

    @JsonProperty("is_day")
    private Integer isDay;

    private ConditionDto condition;
}
