package com.example.randomcoffee.rest_api.dto.response;

import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(include= JsonSerialize.Inclusion.NON_EMPTY)
public class OfficeResponse extends OfficeRequest {

    Long id;
    CountryResponse officeCountry;

    Integer colleaguesNumber;

}
