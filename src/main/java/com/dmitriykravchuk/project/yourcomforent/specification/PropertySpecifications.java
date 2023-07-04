package com.dmitriykravchuk.project.yourcomforent.specification;

import com.dmitriykravchuk.project.yourcomforent.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;

public class PropertySpecifications {

    public static Specification<Property> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<Property> filterByPropertyType(String housingType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("housingType"), HousingType.valueOf(housingType));
    }

    public static Specification<Property> filterByBedCount(Integer bedRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bedRooms"), bedRooms);
    }

    public static Specification<Property> filterByBathCount(Integer bathRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bathRooms"), bathRooms);
    }

    public static Specification<Property> filterByMaxPeoples(Integer maxAmountPeople) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("maxAmountPeople"), maxAmountPeople);
    }

    public static Specification<Property> filterByAvailability(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);
            subquery.select(bookingRoot.get("housing").get("id"))
                    .where(criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(bookingRoot.get("startDate"), endDate),
                            criteriaBuilder.greaterThanOrEqualTo(bookingRoot.get("endDate"), startDate)
                    ));
            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

    public static Specification<Property> filterByQuantityBeds(Integer beds) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("beds"), beds);
    }

    public static Specification<Property> filterByCity(String city) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Housing> housingJoin = root.join("housing");
            Join<Housing, Location> locationJoin = housingJoin.join("location");

            return criteriaBuilder.equal(locationJoin.get("city"), city);
        };
    }
}