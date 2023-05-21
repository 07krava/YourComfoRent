package com.dmitriykravchuk.project.yourcomforent.dto;


import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import com.dmitriykravchuk.project.yourcomforent.model.Location;
import lombok.Data;

@Data
public class LocationDTO {
    private String country;
    private String region;
    private String city;
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    private String zipCode;
    private Housing housing;


    public static LocationDTO convertToDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCountry(location.getCountry());
        locationDTO.setRegion(location.getRegion());
        locationDTO.setCity(location.getCity());
        locationDTO.setStreet(location.getStreet());
        locationDTO.setHouseNumber(location.getHouseNumber());
        locationDTO.setApartmentNumber(location.getApartmentNumber());
        locationDTO.setZipCode(location.getZipCode());
        locationDTO.setHousing(location.getHousing());
        return locationDTO;
    }

    public static Location convertToLocation(LocationDTO locationDTO){
        Location location = new Location();
        location.setCountry(locationDTO.getCountry());
        location.setRegion(locationDTO.getRegion());
        location.setCity(locationDTO.getCity());
        location.setStreet(locationDTO.getStreet());
        location.setHouseNumber(locationDTO.getHouseNumber());
        location.setApartmentNumber(locationDTO.getApartmentNumber());
        location.setZipCode(locationDTO.getZipCode());
        location.setHousing(locationDTO.getHousing());
        return location;
    }
}
