package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.Booking;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import com.dmitriykravchuk.project.yourcomforent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByHousingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Housing housing, Date startDate, Date endDate);

    Optional<Booking> findByHousingAndStartDateAndEndDate(Housing housing, Date startDate, Date endDate);

    Booking findByRenterAndHousingAndStatusNot(User renter, Housing housing, String status);
}