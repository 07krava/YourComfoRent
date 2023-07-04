package com.dmitriykravchuk.project.yourcomforent.service.impl;

import com.dmitriykravchuk.project.yourcomforent.model.Property;
import com.dmitriykravchuk.project.yourcomforent.repository.PropertyRepository;
import com.dmitriykravchuk.project.yourcomforent.service.PropertyService;
import com.dmitriykravchuk.project.yourcomforent.specification.PropertySpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> getFilteredProperties(BigDecimal minPrice,
                                                BigDecimal maxPrice,
                                                String housingType,
                                                Integer bedRooms,
                                                Integer bathRooms,
                                                Integer maxAmountPeople,
                                                Date startDate,
                                                Date endDate,
                                                Integer beds,
                                                String city) {
        Specification<Property> spec = Specification.where(null);

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(PropertySpecifications.filterByPriceRange(minPrice, maxPrice));
        }

        if (housingType != null) {
            spec = spec.and(PropertySpecifications.filterByPropertyType(housingType));
        }

        if (bedRooms != null){
            spec = spec.and(PropertySpecifications.filterByBedCount(bedRooms));
        }

        if (bathRooms != null){
            spec = spec.and(PropertySpecifications.filterByBathCount(bathRooms));
        }

        if(maxAmountPeople != null){
            spec = spec.and(PropertySpecifications.filterByMaxPeoples(maxAmountPeople));
        }

        if (startDate != null && endDate != null) {

            spec = spec.and(PropertySpecifications.filterByAvailability(startDate, endDate));
        }

        if (beds != null){
            spec = spec.and(PropertySpecifications.filterByQuantityBeds(beds));
        }

        if (city != null){
            spec = spec.and(PropertySpecifications.filterByCity(city));
        }

        return propertyRepository.findAll(spec);
    }
}
