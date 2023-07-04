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
import java.util.Optional;

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
        return bookings.isEmpty();
    }

    public Long findPendingBookingId(User user) {
        List<Booking> bookings = user.getBookings();

        for (Booking booking : bookings) {
            if ("pending".equals(booking.getStatus())) {
                return booking.getId();
            }
        }
        return null;
    }

    public Booking createBooking(Booking booking) {
        User renter = userRepository.findById(booking.getRenter().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + booking.getRenter().getId()));
        Housing housing = housingRepository.findById(booking.getHousing().getId())
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + booking.getHousing().getId()));

        BigDecimal totalAmount = housing.getPrice();
        //Поле ниже нужно для добавления этой переменной в базу данных в таблицу booking
        booking.setTotalAmountOfMoney(totalAmount);
        BigDecimal renterBalance = renter.getWallet().getBalance();
        BigDecimal renterFrozenBalance = renter.getWallet().getFrozenBalance();
        String bookingStatus = booking.getStatus();

        Long bookingId = findPendingBookingId(renter);

        if (bookingId != null) {
            Optional<Booking> booking1 = bookingRepository.findById(bookingId);
            bookingStatus = booking1.get().getStatus();
        }

        if (bookingStatus == null) {
            booking.setStatus("StatusNotDefined");
            bookingStatus = "StatusNotDefined";
        }

        if (housing.getOwner().equals(renter)) {
            if (housing.getMaxAmountPeople() == booking.getGuests()) {
                booking.setOwner(renter);
                booking.setHousing(housing);
                booking.setOwner(housing.getOwner());
                booking.setStatus("approved");
                if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                    throw new RuntimeException("Жилье уже забронировано на указанные даты.");
                }
                createNewBooking(booking);
            } else {
                throw new RuntimeException("Превишено максимальное количество проживающих");
            }
        } else if ("approved".equals(bookingStatus)) {
            return booking;
        } else if (renterBalance.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Недостаточно средств. Пожалуйста, пополните свой баланс.");
        } else if ("pending".equals(bookingStatus)) {
            if (renterFrozenBalance.compareTo(BigDecimal.ZERO) > 0) {
                // Перевод средств от renter к housing.getOwner
                BigDecimal requiredBalance = totalAmount.subtract(renterFrozenBalance);
                renter.getWallet().setBalance(renterBalance.subtract(requiredBalance));
                housing.getOwner().getWallet().setBalance(housing.getOwner().getWallet().getBalance().add(totalAmount));
                renter.getWallet().setFrozenBalance(BigDecimal.ZERO);
                booking.setStatus("approved");
                if (housing.getMaxAmountPeople() == booking.getGuests()) {
                    booking.setOwner(renter);
                    booking.setHousing(housing);
                    booking.setOwner(housing.getOwner());
                    Long pendingBookingId = findPendingBookingId(renter);
                    booking.setId(pendingBookingId);
                    updateBookingStatus(booking);
                } else {
                    throw new RuntimeException("Превишено максимальное количество проживающих");
                }
            } else if (renterBalance.compareTo(totalAmount) >= 0) {
                // Перевод средств от renter к housing.getOwner
                renter.getWallet().setBalance(renterBalance.subtract(totalAmount));
                housing.getOwner().getWallet().setBalance(housing.getOwner().getWallet().getBalance().add(totalAmount));
                booking.setStatus("approved");
                updateBookingStatus(booking); // Обновление статуса бронирования в базе данныхelse {
                if (renterBalance.compareTo(BigDecimal.ZERO) > 0) {
                    // Перенос средств на замороженный счет
                    BigDecimal remainingFrozenBalance = totalAmount.subtract(renterBalance);
                    renter.getWallet().setFrozenBalance(remainingFrozenBalance);
                    renter.getWallet().setBalance(BigDecimal.ZERO);
                    booking.setStatus("pending");
                    if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                        throw new RuntimeException("Жилье уже забронировано на указанные даты.");
                    }
                    createNewBooking(booking); // Создание нового бронирования в базе данных
                } else {
                    throw new RuntimeException("Недостаточно средств. Пожалуйста, пополните свой баланс.");
                }
            }
        } else if ("StatusNotDefined".equals(bookingStatus)) {
            if (renterFrozenBalance.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal availableBalance = renterFrozenBalance.add(renterBalance);
                if (availableBalance.compareTo(totalAmount) >= 0) {
                    // Перевод средств от renter к housing.getOwner
                    BigDecimal requiredBalance = totalAmount.subtract(renterFrozenBalance);
                    renter.getWallet().setBalance(renterBalance.subtract(requiredBalance));
                    housing.getOwner().getWallet().setBalance(housing.getOwner().getWallet().getBalance().add(totalAmount));
                    renter.getWallet().setFrozenBalance(BigDecimal.ZERO);
                    booking.setStatus("approved");
                    updateBookingStatus(booking); // Обновление статуса бронирования в базе данных
                }
            } else if (renterBalance.compareTo(totalAmount) >= 0) {
                // Перевод средств от renter к housing.getOwner
                renter.getWallet().setBalance(renterBalance.subtract(totalAmount));
                housing.getOwner().getWallet().setBalance(housing.getOwner().getWallet().getBalance().add(totalAmount));
                booking.setStatus("approved");
                if (housing.getMaxAmountPeople() == booking.getGuests()) {
                    booking.setOwner(renter);
                    booking.setHousing(housing);
                    booking.setOwner(housing.getOwner());
                    if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                        throw new RuntimeException("Жилье уже забронировано на указанные даты.");
                    }
                    createNewBooking(booking);
                } else {
                    throw new RuntimeException("Превишено максимальное количество проживающих");
                }

            } else if (renterBalance.compareTo(BigDecimal.ZERO) > 0 && renterBalance.compareTo(totalAmount) < 0) {
                // Перенос средств на замороженный счет
                renter.getWallet().setFrozenBalance(renterBalance);
                renter.getWallet().setBalance(BigDecimal.ZERO);
                booking.setStatus("pending");
                booking.setOwner(renter);
                booking.setHousing(housing);
                booking.setOwner(housing.getOwner());
                if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
                    throw new RuntimeException("Жилье уже забронировано на указанные даты.");
                }
                createNewBooking(booking);

            } else {
                throw new RuntimeException("Недостаточно средств. Пожалуйста, пополните свой баланс.");
            }

        } else {
            throw new RuntimeException("Неопределенный статус бронирования.");
        }

        return booking;
    }

    private void createNewBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    private void updateBookingStatus(Booking booking) {
        Optional<Booking> existingBooking = bookingRepository.findById(booking.getId());
        if (existingBooking.isPresent()) {
            existingBooking.get().setStatus(booking.getStatus());
            bookingRepository.save(existingBooking.get());
        } else {
            throw new RuntimeException("Не удаётся найти бронирование в базе данных.");
        }
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        List<Booking> listBooking = user.getBookings();
        return listBooking;
    }
}
