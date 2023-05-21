package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.Booking;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByHousingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Housing housing, Date startDate, Date endDate);
}
