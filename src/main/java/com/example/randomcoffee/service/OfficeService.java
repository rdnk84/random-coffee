package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;

public interface OfficeService {

    OfficeResponse getOfficeById(Long id);
    OfficeResponse createOffice(OfficeRequest request);
    void deleteOffice(Long id);
    OfficeResponse updateOffice(Long id, OfficeRequest request);
}
