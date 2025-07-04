package com.mit.StayNest.Services;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setStatus("BOOKED");
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByTenant(User tenant) {
        return bookingRepository.findByTenant(tenant);
    }

    @Override
    public List<Booking> getBookingsByListing(Listing listing) {
        return bookingRepository.findByListing(listing);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status.toUpperCase());
    }

    @Override
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}
