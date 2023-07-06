package com.dmitriykravchuk.project.yourcomforent.service.impl;

import com.dmitriykravchuk.project.yourcomforent.model.Booking;
import com.dmitriykravchuk.project.yourcomforent.model.Housing;
import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.repository.BookingRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.HousingRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.UserRepository;
import com.dmitriykravchuk.project.yourcomforent.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final HousingRepository housingRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(HousingRepository housingRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public boolean isHousingAvailableByDates(Housing housing, Date startDate, Date endDate) {
        List<Booking> bookings = bookingRepository.findByHousingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(housing, startDate, endDate);

        for (Booking booking: bookings){
            if ("rejected".equals(booking.getStatus())){
                return true;  // Если найдено отклоненное бронирование, разрешаем бронирование на указанные даты
            }
        }
        return bookings.isEmpty();
    }

    public Booking createBooking(Booking booking){
        User renter = userRepository.findById(booking.getRenter().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + booking.getRenter().getId()));
        Housing housing = housingRepository.findById(booking.getHousing().getId())
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + booking.getHousing().getId()));

        BigDecimal totalAmount = housing.getPrice();
        //Поле ниже нужно для добавления этой переменной в базу данных в таблицу booking
        booking.setTotalAmountOfMoney(totalAmount);
        BigDecimal renterBalance = renter.getWallet().getBalance();

        if (housing.getOwner().equals(renter)) {
            if (housing.getMaxAmountPeople() == booking.getGuests()) {
                booking.setOwner(renter);
                booking.setHousing(housing);
                booking.setOwner(housing.getOwner());
                booking.setStatus("approved");
                if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                    throw new RuntimeException("The housing is already booked for the dates indicated.");
                }
                createNewBooking(booking);
            } else {
                throw new RuntimeException("Maximum number of occupants exceeded");
            }
        }else if (renterBalance.compareTo(totalAmount) >= 0) {
            renter.getWallet().setBalance(renterBalance.subtract(totalAmount));
            housing.getOwner().getWallet().setFrozenBalance(housing.getOwner().getWallet().getFrozenBalance().add(totalAmount));
            if (housing.getMaxAmountPeople() == booking.getGuests()) {
                booking.setOwner(renter);
                booking.setHousing(housing);
                booking.setOwner(housing.getOwner());
                if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                    throw new RuntimeException("The housing is already booked for the dates indicated.");
                }
                booking.setStatus("pending");
                createNewBooking(booking);
            } else {
                throw new RuntimeException("Maximum number of occupants exceeded.");
            }
        } else {
            throw new RuntimeException("Not enough money in your account. Please top up your account.");
        }

        return booking;
    }

    private void createNewBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        List<Booking> listBooking = user.getBookings();
        return listBooking;
    }

    @Override
    public List<Booking> getBookingsForOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));

        List<Booking> bookings = owner.getBookings();
        if (bookings == null) {
            throw new RuntimeException("The owner hasn't any bookings.");
        }

        return bookingRepository.findAll();
    }

    public void approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!"pending".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot approve a booking that is not in pending status.");
        }

        User owner = booking.getHousing().getOwner();

        // Перевод средств от renter к owner
        owner.getWallet().setBalance(owner.getWallet().getBalance().add(owner.getWallet().getFrozenBalance()));
        owner.getWallet().setFrozenBalance(BigDecimal.ZERO);

        booking.setStatus("approved");
        bookingRepository.save(booking);
    }

    public void rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!"pending".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot reject a booking that is not in pending status.");
        }

        BigDecimal totalAmount = booking.getHousing().getPrice();
        User renter = booking.getRenter();
        User owner = booking.getOwner();

        // Возврат средств на счет renter
        renter.getWallet().setBalance(renter.getWallet().getBalance().add(totalAmount));
        owner.getWallet().setFrozenBalance(BigDecimal.ZERO);

        booking.setStatus("rejected");
        bookingRepository.save(booking);
    }
}
