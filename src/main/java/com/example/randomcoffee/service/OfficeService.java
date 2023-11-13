package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;

public interface OfficeService {

    OfficeResponse getOfficeById(Long id);
    OfficeResponse createOffice(OfficeRequest request);
    void deleteOffice(Long id);
    OfficeResponse updateOffice(Long id, OfficeRequest request);
    OfficeResponse userToOffice(Long officeId, Long userId);
}
