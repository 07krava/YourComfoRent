package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByHousingId(Long housingId);
}
