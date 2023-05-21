package com.dmitriykravchuk.project.yourcomforent.controller;

import com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO;
import com.dmitriykravchuk.project.yourcomforent.dto.ImageDTO;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import com.dmitriykravchuk.project.yourcomforent.service.HousingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/housing")
public class HousingController {

    @Autowired
    private HousingService housingService;

    @PostMapping("/add")
    public ResponseEntity<HousingDTO> createHousing(@ModelAttribute HousingDTO housing, @RequestParam("file") MultipartFile[] files) throws IOException, IOException {
        HousingDTO newHousing = housingService.createHousing(housing, files);
        return new ResponseEntity<>(newHousing, HttpStatus.OK);
    }

    @GetMapping("/searchByCity/{city}")
    public List<Housing> searchByCity(@PathVariable("city") String city, @ModelAttribute Housing housing) {
        return housingService.findByCity(city);
    }

    @GetMapping("/allHousing")
    public List<HousingDTO> getAllHousing() {
        return housingService.getAllHousing();
    }

    @PutMapping("/updateHousing/{id}")
    public HousingDTO updateHousing(@PathVariable Long id, @ModelAttribute HousingDTO housingDTO, @RequestParam("file") MultipartFile[] files) throws IOException {
        housingDTO.setId(id);
        return housingService.updateHousing(id, housingDTO, files);
    }

    @DeleteMapping("/deleteHousing/{id}")
    public ResponseEntity<String> deleteHousingById(@PathVariable Long id) {
        housingService.deleteHousing(id);
        return new ResponseEntity<>("Housing "+ id + " delete successfully!", HttpStatus.OK);
    }

    @GetMapping("/getHousing/{id}")
    public HousingDTO getHousingById(@PathVariable Long id) {
        return housingService.getHousingById(id);
    }

    @GetMapping("/getImagesByHousing/{id}")
    public List<ImageDTO> getImageByHousingId(@PathVariable Long id){
        return housingService.getImagesByHousingId(id);
    }

    @GetMapping("/{housingId}/image/{imageId}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long housingId, @PathVariable Long imageId) {
        log.info("Here start method getPhotoById");
        HousingDTO housing = housingService.getHousingById(housingId);
        ImageDTO image = null;
        if (housing.getImages() != null ) {
            for (ImageDTO p : housing.getImages()) {
                if (p.getId().equals(imageId) && p.getId() != null) {
                    image = p;
                    break;
                }
            }
        }
        if (image == null){
            log.info("This message you can see if your image not found");
            throw new EntityNotFoundException("Image not found with this id: " + imageId);
        }
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{housingId}/deleteImage/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long housingId, @PathVariable Long imageId) {
        housingService.deleteImageByIdFromHousingId(housingId, imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public List<Housing> getAvailableHousing(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return housingService.getAvailableHousings(startDate, endDate);
    }

    @GetMapping("/booked")
    public List<Housing> getBookedHousing(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return housingService.getBookedHousing(startDate, endDate);
    }

    @GetMapping("/findByMaxPeople/{maxValuePeople}")
    public List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(@PathVariable("maxValuePeople") int maxValuePeople, @ModelAttribute Housing housing) {
        return housingService.findByMaxNumberOfPeopleThatCanBeAccommodated(maxValuePeople);
    }
}
