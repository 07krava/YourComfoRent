package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO;
import com.dmitriykravchuk.project.yourcomforent.dto.ImageDTO;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import com.dmitriykravchuk.project.yourcomforent.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface HousingService {

    List<Housing> findByCity(String city);

    HousingDTO createHousing(HousingDTO housingDTO, MultipartFile[] files) throws IOException;

    HousingDTO updateHousing(Long id, HousingDTO housingDTO, MultipartFile[] files) throws IOException;

    List<ImageDTO> getImagesByHousingId(Long housingId);

    Image getImageById(Long housingId, Long imageId);

    List<HousingDTO> getAllHousing();

    HousingDTO getHousingById(Long id);

    void deleteHousing(Long id);

    void deleteImageByIdFromHousingId(Long housingId, Long imageId);

    List<Housing> getBookedHousing(Date startDate, Date endDate);

    List<Housing> getAvailableHousings(Date startDate, Date endDate);

    List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(int maxAmountPeople);
}
