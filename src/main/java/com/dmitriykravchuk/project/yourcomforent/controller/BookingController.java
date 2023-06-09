package com.dmitriykravchuk.project.yourcomforent.controller;

import com.dmitriykravchuk.project.yourcomforent.model.Booking;
import com.dmitriykravchuk.project.yourcomforent.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/housing/bookings")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/allBookingsById/{id}")
    public List<Booking> getBookingsByUserId(@PathVariable Long id) {
        return bookingService.getBookingsByUserId(id);
    }
}
