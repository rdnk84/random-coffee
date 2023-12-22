package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.entity.Office;
import com.example.randomcoffee.model.db.repository.CountryRepo;
import com.example.randomcoffee.model.db.repository.OfficeRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.model.enums.OfficeStatus;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.OfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepo officeRepo;
    private final UserRepo userRepo;
    private final ObjectMapper mapper;
    private final CountryRepo countryRepo;


    @Override
    public OfficeResponse getOfficeDto(Long id) {
        String errorMsg = String.format("Office with id %d not found", id);
        Office office = officeRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        OfficeResponse result = mapper.convertValue(office, OfficeResponse.class);
        return result;
    }



    @Override
    public OfficeResponse createOffice(OfficeRequest request) {
        String title = request.getCountryName();
        String errorMsg = String.format("Office  with country  %s not found", title);
        Country country = countryRepo.findByTitle(title).orElseThrow(
                () -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        Office office = mapper.convertValue(request, Office.class);
        office.setCountry(country);
        office.setCreatedAt(LocalDateTime.now());
        office.setStatus(OfficeStatus.CREATED);
        Office save = officeRepo.save(office);
        country.getOffices().add(office);
        countryRepo.save(country);
        CountryResponse countryResponse = mapper.convertValue(country, CountryResponse.class);
        OfficeResponse result = mapper.convertValue(save, OfficeResponse.class);
        result.setCountryName(country.getTitle());
        result.setOfficeCountry(countryResponse);
        return result;
    }

    @Override
    public void deleteOffice(Long id) {
        String errorMsg = String.format("Office with id %d not found", id);
        Office office = officeRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!office.getStatus().equals(OfficeStatus.DELETED)) {
            office.setStatus(OfficeStatus.DELETED);
            office.setUpdatedAt(LocalDateTime.now());
            officeRepo.save(office);
            return;
        }
        throw new CustomException("This office had already been deleted", HttpStatus.NOT_FOUND);
    }

    @Override
    public OfficeResponse updateOffice(Long id, OfficeRequest request) {

        String errorMsg = String.format("Office with id %d not found", id);
        Office office = officeRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!office.getStatus().equals(OfficeStatus.DELETED)) {
            if (!StringUtils.isBlank(request.getCountryName())) {
                String title = request.getCountryName();
                String errMsg = String.format("Office  with country  %s not found", title);
                Country country = countryRepo.findByTitle(title).orElseThrow(
                        () -> new CustomException(errMsg, HttpStatus.NOT_FOUND));
                office.setCountry(country);
            } else {
                office.setCountry(office.getCountry());
            }
            office.setCity(StringUtils.isBlank(request.getCity()) ? office.getCity() : request.getCity());
            office.setStatus(OfficeStatus.UPDATED);
            office.setUpdatedAt(LocalDateTime.now());
            Office save = officeRepo.save(office);
            OfficeResponse result = mapper.convertValue(save, OfficeResponse.class);
            result.setCountryName(office.getCountry().getTitle());
            return result;
        }
        throw new CustomException("This office had already been deleted", HttpStatus.NOT_FOUND);
    }


    public OfficeResponse userToOffice(Long officeId, Long userId) {
        String userNotFound = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        if (user.getOffice() == null) {
            String officeNotFound = String.format("Office with id %d not found", userId);
            Office office = officeRepo.findById(officeId).orElseThrow(() -> new CustomException(officeNotFound, HttpStatus.NOT_FOUND));
            office.getColleagues().add(user);
            office.setUpdatedAt(LocalDateTime.now());
            office.setStatus(OfficeStatus.UPDATED);
            user.setOffice(office);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);
            Office save = officeRepo.save(office);

            OfficeResponse result = mapper.convertValue(save, OfficeResponse.class);
            result.setCountryName(office.getCountry().getTitle());
            result.setColleaguesNumber(office.getColleagues().size());
            return result;
        }
        throw new CustomException("This user is attached to another office; please call changeOffice() method", HttpStatus.BAD_REQUEST);
    }

}
