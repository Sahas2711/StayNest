package com.mit.StayNest.Services;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BookingServiceImpl implements BookingService {
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {
    	logger.info("Creating booking for listing ID: {} by tenant ID: {}",
                booking.getListing().getId(), booking.getTenant().getId());
        booking.setStatus("BOOKED");

        Booking saved = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: {}", saved.getId());
        return saved;
        
    }

    @Override
    public List<Booking> getBookingsByTenant(User tenant) {
    	 logger.info("Fetching bookings for tenant ID: {}", tenant.getId());
        return bookingRepository.findByTenant(tenant);
    }

    @Override
    public List<Booking> getBookingsByListing(Listing listing) {
    	 logger.info("Fetching bookings for listing ID: {}", listing.getId());
        return bookingRepository.findByListing(listing);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
    	logger.info("Fetching booking by ID: {}", id);
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
    	logger.info("Fetching bookings with status: {}", status.toUpperCase());
        return bookingRepository.findByStatus(status.toUpperCase());
    }

    @Override
    public Booking cancelBooking(Long id) {
    	logger.info("Cancellation request received for booking ID: {}", id);

        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Booking not found with ID: {}", id);
                return new RuntimeException("Booking not found with ID: " + id);
            });
        booking.setStatus("CANCELLED");
        Booking cancelled = bookingRepository.save(booking);
        logger.info("Booking cancelled successfully with ID: {}", cancelled.getId());
        return cancelled;
    }
}
