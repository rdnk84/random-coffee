package com.example.randomcoffee.rest_api.dto.request;

import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeRequest {

    String city;
    Long countryId;
    String countryName;

}
