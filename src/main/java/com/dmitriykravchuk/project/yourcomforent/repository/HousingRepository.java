package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingRepository extends JpaRepository<Housing, Long> {
}
