package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.entity.Office;
import com.example.randomcoffee.model.db.repository.CountryRepo;
import com.example.randomcoffee.model.db.repository.OfficeRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.OfficeStatus;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OfficeServiceImplTest {

    @InjectMocks
    private OfficeServiceImpl officeService;

    @Spy
    private ObjectMapper mapper;

    @Mock
    private OfficeRepo officeRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CountryRepo countryRepo;

    @Test
    void getOfficeById() {
        Office office = new Office();
        office.setId(1L);
        when(officeRepo.findById(office.getId())).thenReturn(Optional.of(office));
        OfficeResponse result = officeService.getOfficeById(office.getId());
        assertEquals(Optional.of(result.getId()), Optional.of(office.getId()));
    }

    //    @Test
//    public void wrongInput() {
//        Throwable exception = assertThrows(InvalidArgumentException.class,
//                ()->{objectName.yourMethod("WRONG");} );
//    }
    @Test
    void country_not_found() {
        OfficeRequest request = new OfficeRequest();
        request.setCountryName("RF");
        Country country = new Country();
        country.setId(1L);
        country.setTitle("US");
        when(countryRepo.save(any(Country.class))).thenReturn(country);
        when(countryRepo.findByTitle(anyString())).thenThrow(CustomException.class);
        Office office = new Office();
        office.setId(1L);
        when(officeRepo.save(any(Office.class))).thenReturn(office);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            officeService.createOffice(request);
        }, "Office with this country not found");

    }

    @Test
    void createOffice() {
        OfficeRequest request = new OfficeRequest();
        request.setCity("Mosc");
        request.setCountryName("RF");

        Country country = new Country();
        country.setId(1L);
        country.setTitle("RF");
//        List<Office> offices = new ArrayList<>();
        country.setOffices(new ArrayList<>());
        when(countryRepo.findByTitle(anyString())).thenReturn(Optional.of(country));

        Office office = new Office();
        office.setId(1L);
        List<Office> offices = country.getOffices();
        offices.add(office);
//        Set<Office> officeSet = offices.stream().collect(Collectors.toSet());
        country.setOffices(offices);
        office.setCountry(country);

//        office.setCountry(new Country());
        when(officeRepo.save(any(Office.class))).thenReturn(office);
        when(countryRepo.save(any(Country.class))).thenReturn(country);
        OfficeResponse result = officeService.createOffice(request);
        assertEquals(Optional.of(result.getId()), Optional.of(office.getId()));
        assertEquals(Optional.of(country.getOffices()), Optional.of(offices));
    }

    @Test
    void deleteOffice() {
        Office office = new Office();
        office.setId(1L);
        office.setStatus(OfficeStatus.CREATED);

        when(officeRepo.findById(1L)).thenReturn(Optional.of(office));
        officeService.deleteOffice(1L);
        verify(officeRepo, times(1)).save(any(Office.class));
        assertEquals(OfficeStatus.DELETED, office.getStatus());
    }

    @Test
    void delete_status_deleted() {
        Office office = new Office();
        office.setId(1L);
        office.setStatus(OfficeStatus.DELETED);

        when(officeRepo.save(any(Office.class))).thenReturn(office);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            officeService.deleteOffice(office.getId());
        }, "This office had already been deleted");
        assertEquals(CustomException.class, thrown.getClass());
    }

    @Test
    void updateOffice() {
        OfficeRequest request = new OfficeRequest();
        request.setCity("SPb");

        Office office = new Office();
        office.setCity("Moscow");
        office.setStatus(OfficeStatus.CREATED);
        office.setId(1L);

        Country country = new Country();
        country.setId(1L);
        country.setTitle("RF");

        office.setCountry(country);

        when(officeRepo.findById(office.getId())).thenReturn(Optional.of(office));
//        when(countryRepo.findByTitle(country.getTitle())).thenReturn(Optional.of(country));
        when(officeRepo.save(any(Office.class))).thenReturn(office);
        OfficeResponse result = officeService.updateOffice(office.getId(), request);

        assertNotEquals(request.getCountryName(), result.getCountryName());
        assertEquals(result.getCity(), request.getCity());
    }

    @Test
    void updateOffice_country_empty() {
        OfficeRequest request = new OfficeRequest();
        request.setCity("SPb");
//        request.setCountryName("RF");
        request.setCountryName("");

        Office office = new Office();
        office.setCity("Moscow");
        office.setStatus(OfficeStatus.CREATED);
        office.setId(5L);

        Country country = new Country();
        country.setId(5L);
        country.setTitle("RF");

        office.setCountry(country);

        when(officeRepo.findById(office.getId())).thenReturn(Optional.of(office));
        when(countryRepo.findByTitle(country.getTitle())).thenReturn(Optional.of(country));

//        office.setCountry(request.getCountryName());
        when(officeRepo.save(any(Office.class))).thenReturn(office);
        OfficeResponse result = officeService.updateOffice(office.getId(), request);

        assertNotEquals(result.getCountryName(), request.getCountryName());
//        assertEquals(result.getCountryName(), request.getCountryName());
    }

    @Test
    void updateOffice_emptyFields() {
        OfficeRequest request = new OfficeRequest();
        Office office = new Office();
        office.setId(1L);
        when(officeRepo.save(any(Office.class))).thenReturn(office);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            officeService.updateOffice(office.getId(), request);
        }, "This office had already been deleted");
    }

    @Test
    void userToOffice() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);

        Office office = new Office();
        office.setId(2L);
        Set<CoffeeUser> colleagues = new HashSet<>();
        office.setColleagues(colleagues);

        Country country = new Country();
        country.setTitle("RF");

        colleagues.add(user);
        office.setColleagues(colleagues);
        office.setCountry(country);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(officeRepo.findById(office.getId())).thenReturn(Optional.of(office));
        when(officeRepo.save(any(Office.class))).thenReturn(office);

        OfficeResponse result = officeService.userToOffice(office.getId(), user.getId());
        assertEquals(result.getColleaguesNumber(), office.getColleagues().size());

    }

}