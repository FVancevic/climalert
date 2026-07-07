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
public class LocationDto {

    private String name;
    private String region;
    private String country;
    private Double lat;
    private Double lon;

    @JsonProperty("tz_id")
    private String tzId;

    @JsonProperty("localtime")
    private String localtime;
}
