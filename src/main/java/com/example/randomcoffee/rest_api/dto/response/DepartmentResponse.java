package com.example.randomcoffee.rest_api.dto.response;

import com.example.randomcoffee.rest_api.dto.request.DepartmentRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentResponse extends DepartmentRequest {

    Long id;
}
