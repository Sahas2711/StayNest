package com.mit.StayNest.Services;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByTenant(User tenant);

    List<Booking> getBookingsByListing(Listing listing);

    Optional<Booking> getBookingById(Long id);

    List<Booking> getBookingsByStatus(String status);

    Booking cancelBooking(Long id);
    
    double getRentForBooking(Long id , double month);
}
