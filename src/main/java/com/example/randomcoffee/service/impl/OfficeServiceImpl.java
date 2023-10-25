package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.model.db.entity.Office;
import com.example.randomcoffee.model.db.repository.OfficeRepo;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.service.OfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepo officeRepo;
    private final ObjectMapper mapper;

    @Override
    public OfficeResponse getOfficeById(Long id) {
        Office office = officeRepo.findById(id).orElse(new Office());
        OfficeResponse result = mapper.convertValue(office, OfficeResponse.class);
        return result;
    }

    @Override
    public OfficeResponse createOffice(OfficeRequest request) {

        Office office = mapper.convertValue(request, Office.class);
        office.setCreatedAt(LocalDateTime.now());
        Office save = officeRepo.save(office);
        OfficeResponse result = mapper.convertValue(save, OfficeResponse.class);
        return result;
    }

    @Override
    public void deleteOffice(Long id) {

    }

    @Override
    public OfficeResponse updateOffice(Long id, OfficeRequest request) {
        return null;
    }
}
