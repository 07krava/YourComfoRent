package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.model.Booking;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;

import java.util.Date;
import java.util.List;

public interface BookingService {

    boolean isHousingAvailableByDates(Housing housing, Date startDate, Date endDate);

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByUserId(Long userId);

    List<Booking> getBookingsForOwner(Long ownerId);

    void approveBooking(Long bookingId);

    void rejectBooking(Long bookingId);
}
