package com.dmitriykravchuk.project.yourcomforent.controller;

import com.dmitriykravchuk.project.yourcomforent.model.Property;
import com.dmitriykravchuk.project.yourcomforent.service.PropertyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<Property> getFilteredProperties(@RequestParam(required = false) BigDecimal minPrice,
                                                @RequestParam(required = false) BigDecimal maxPrice,
                                                @RequestParam(required = false) String housingType,
                                                @RequestParam(required = false) Integer bedRooms,
                                                @RequestParam(required = false) Integer bathRooms,
                                                @RequestParam(required = false) Integer maxAmountPeople,
                                                @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                @RequestParam(required = false) Integer beds,
                                                @RequestParam(required = false) String city
    ){
        return propertyService.getFilteredProperties(
                minPrice,
                maxPrice,
                housingType,
                bedRooms,
                bathRooms,
                maxAmountPeople,
                startDate,
                endDate,
                beds,
                city);
    }
}
