package com.dmitriykravchuk.project.yourcomforent.service.impl;

import com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO;
import com.dmitriykravchuk.project.yourcomforent.dto.ImageDTO;
import com.dmitriykravchuk.project.yourcomforent.model.*;
import com.dmitriykravchuk.project.yourcomforent.repository.BookingRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.HousingRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.ImageRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.UserRepository;
import com.dmitriykravchuk.project.yourcomforent.service.HousingService;
import com.dmitriykravchuk.project.yourcomforent.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO.convertToDTO;
import static com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO.convertToEntity;
import static com.dmitriykravchuk.project.yourcomforent.dto.ImageDTO.convertToImage;

@Slf4j
@Service
public class HousingServiceImpl implements HousingService {

    private final HousingRepository housingRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public HousingServiceImpl(HousingRepository housingRepository, BookingRepository bookingRepository, ImageRepository imageRepository, ImageService imageService, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Housing> findByCity(String city) {
        List<Housing> housings = housingRepository.findAll();
        List<Housing> result = new ArrayList<>();
        for (Housing housing : housings) {
            Location location = housing.getLocation();
            if (location != null && location.getCity().equals(city)) {
                result.add(housing);
            }
        }
        return result;
    }

    @Override
    public List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(int maxAmountPeople) {
        List<Housing> housings = housingRepository.findAll();
        List<Housing> result = new ArrayList<>();
        for (Housing housing : housings) {
            if (housing.getMaxAmountPeople() == maxAmountPeople) {
                result.add(housing);
            }
        }
        return result;
    }

    @Override
    public HousingDTO createHousing(HousingDTO housingDTO, MultipartFile[] files, Long ownerId) throws IOException {

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("Owner not found by id " ));

        Housing housing = new Housing();
        housing.setDescription(housingDTO.getDescription());
        housing.setTitle(housingDTO.getTitle());
        housing.setMaxAmountPeople(housingDTO.getMaxAmountPeople());
        housing.setBeds(housingDTO.getBeds());
        housing.setBedRooms(housingDTO.getBedRooms());
        housing.setBathRooms(housingDTO.getBathRooms());
        housing.setPrice(housingDTO.getPrice());
        housing.setHousingType(housingDTO.getHousingType());
        housing.setActive(housingDTO.isActive());

        owner.getRoles().add(Role.OWNER);
        housing.setOwner(owner);

        housingRepository.save(housing);

        // Создание и сохранение объекта Location
        Location location = new Location();
        location.setCountry(housingDTO.getLocation().getCountry());
        location.setRegion(housingDTO.getLocation().getRegion());
        location.setCity(housingDTO.getLocation().getCity());
        location.setStreet(housingDTO.getLocation().getStreet());
        location.setHouseNumber(housingDTO.getLocation().getHouseNumber());
        location.setApartmentNumber(housingDTO.getLocation().getApartmentNumber());
        location.setZipCode(housingDTO.getLocation().getZipCode());
        location.setHousing(housing);
        housing.setLocation(location);

        List<Image> imageEntities = new ArrayList<>();
        for (MultipartFile file : files) {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setFileName(file.getOriginalFilename());
            imageDTO.setData(file.getBytes());
            imageDTO.setHousing(housing);
            imageEntities.add(convertToImage(imageDTO));
        }
        housing.setImages(imageEntities);

        housingRepository.save(housing);

        return convertToDTO(housing);
    }

    @Override
    public HousingDTO updateHousing(Long housingId, HousingDTO housingDTO, MultipartFile[] files, Long ownerId) throws IOException {
        Housing housingEntity = housingRepository.findById(housingId)
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + housingId));

        // Check if the owner of the housing matches the provided ownerId
        if (!housingEntity.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You are not authorized to update this housing.");
        }

        // Update fields of HousingEntity based on HousingDTO
        housingEntity.setTitle(housingDTO.getTitle());
        housingEntity.setDescription(housingDTO.getDescription());
        housingEntity.setMaxAmountPeople(housingDTO.getMaxAmountPeople());
        housingEntity.setBeds(housingDTO.getBeds());
        housingEntity.setBedRooms(housingDTO.getBedRooms());
        housingEntity.setBathRooms(housingDTO.getBathRooms());
        housingEntity.setPrice(housingDTO.getPrice());
        housingEntity.setHousingType(housingDTO.getHousingType());
        housingEntity.setActive(housingDTO.isActive());

        Location location = housingEntity.getLocation();
        if (location != null) {
            location.setCountry(housingDTO.getLocation().getCountry());
            location.setRegion(housingDTO.getLocation().getRegion());
            location.setCity(housingDTO.getLocation().getCity());
            location.setStreet(housingDTO.getLocation().getStreet());
            location.setHouseNumber(housingDTO.getLocation().getHouseNumber());
            location.setApartmentNumber(housingDTO.getLocation().getApartmentNumber());
            location.setZipCode(housingDTO.getLocation().getZipCode());
        } else {
            location = new Location();
            location.setCountry(housingDTO.getLocation().getCountry());
            location.setRegion(housingDTO.getLocation().getRegion());
            location.setCity(housingDTO.getLocation().getCity());
            location.setStreet(housingDTO.getLocation().getStreet());
            location.setHouseNumber(housingDTO.getLocation().getHouseNumber());
            location.setApartmentNumber(housingDTO.getLocation().getApartmentNumber());
            location.setZipCode(housingDTO.getLocation().getZipCode());
            location.setHousing(convertToEntity(housingDTO));
            housingDTO.setLocation(location);
        }

        // Update photos of HousingEntity based on files
        if (files != null && files.length > 0) {
            List<Image> imageEntities = new ArrayList<>();
            for (MultipartFile file : files) {

                List<Image> imageList = housingEntity.getImages();
                for (Image imageDTO1 : imageList) {
                    imageDTO1.setId(imageDTO1.getId());
                    imageDTO1.setFileName(file.getOriginalFilename());
                    imageDTO1.setData(file.getBytes());
                    imageEntities.add(imageDTO1);
                }
            }
            housingEntity.setImages(imageEntities);
        }
        Housing savedHousing = housingRepository.save(housingEntity);

        return convertToDTO(savedHousing);
    }

    @Override
    public List<ImageDTO> getImagesByHousingId(Long housingId) {
        List<ImageDTO> imageDTOList = new ArrayList<>();
        Optional<Housing> housingOptional = housingRepository.findById(housingId);
        if (housingOptional.isPresent()) {
            Housing housing = housingOptional.get();
            List<Image> photos = housing.getImages();
            if (photos != null) {
                imageDTOList = photos.stream()
                        .map(photo -> ImageDTO.builder()
                                .id(photo.getId())
                                .fileName(photo.getFileName())
                                .data(photo.getData())
                                .build())
                        .collect(Collectors.toList());
            }
        }
        return imageDTOList;
    }

    @Override
    public Image getImageById(Long housingId, Long imageId) {
        log.info("Start method getPhotoByIdFromHousingId");
        Housing housing = housingRepository.findById(housingId).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + housingId));
        Image image = null;
        if (housing.getImages() != null) {
            for (Image p : housing.getImages()) {
                if (p.getId().equals(imageId) && p.getId() != null) {
                    image = p;
                    log.info("We found our image by id " + image);
                    break;
                }
            }
        } else {
            throw new EntityNotFoundException(" Image this id not found ");
        }
        return image;
    }

    @Override
    public List<HousingDTO> getAllHousing() {
        List<Housing> housingEntities = housingRepository.findAll();
        List<HousingDTO> housingDTOS = new ArrayList<>();

        for (Housing housingEntity : housingEntities) {
            housingDTOS.add(convertToDTO(housingEntity));
        }

        return housingDTOS;
    }

    @Override
    public void deleteHousing(Long id) {
        Optional<Housing> housingEntityOptional = housingRepository.findById(id);

        if (housingEntityOptional.isPresent()) {
            Housing housingEntity = housingEntityOptional.get();
            housingRepository.delete(housingEntity);
            System.out.println("Housing delete successfully");
        } else {
            throw new NullPointerException("Housing not found with id: " + id);
        }
    }

    @Override
    public void deleteImageByIdFromHousingId(Long housingId, Long imageId) {
        Housing housing = housingRepository.findById(housingId).orElseThrow(NullPointerException::new);
        Image image = housing.getImages().stream()
                .filter(p -> p.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Image not found with id " + imageId));
        housing.getImages().remove(image);
        imageRepository.deleteById(imageId);
    }

    @Override
    public HousingDTO getHousingById(Long id) {
        Optional<Housing> housingEntityOptional = housingRepository.findById(id);
        if (housingEntityOptional.isPresent()) {
            Housing housingEntity = housingEntityOptional.get();
            return convertToDTO(housingEntity);
        } else {
            throw new NullPointerException("Housing not found with id: " + id);
        }
    }

    @Override
    public List<Housing> getAvailableHousings(Date startDate, Date endDate) {
        List<Housing> allHousing = housingRepository.findAll();
        Set<Housing> bookedHousing = new HashSet<>();
        for (Housing housing : allHousing) {
            for (Booking booking : housing.getBookings()) {
                if (isOverlapping(booking.getStartDate(), booking.getEndDate(), startDate, endDate)) {
                    bookedHousing.add(housing);
                    break;
                }
            }
        }
        allHousing.removeAll(bookedHousing);
        return allHousing;
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    @Override
    public List<Housing> getBookedHousing(Date startDate, Date endDate) {
        List<Housing> allHousing = housingRepository.findAll();
        Set<Housing> bookedHousing = new HashSet<>();
        for (Housing housing : allHousing) {
            for (Booking booking : housing.getBookings()) {
                if (isOverlapping(booking.getStartDate(), booking.getEndDate(), startDate, endDate)) {
                    bookedHousing.add(housing);
                    break;
                }
            }
        }
        allHousing.removeAll(bookedHousing);
        return new ArrayList<>(bookedHousing);
    }
}
