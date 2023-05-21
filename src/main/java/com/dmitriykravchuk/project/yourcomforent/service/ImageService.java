package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.dto.ImageDTO;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    ImageDTO saveImage(MultipartFile file, Housing housing) throws IOException;

    List<ImageDTO> saveImages(MultipartFile[] files, Housing housing) throws IOException;

    ImageDTO getImageById(Long imageId);

    void deleteImageById(Long id);

    void deleteImagesByHousingId(Long housingId);
}
