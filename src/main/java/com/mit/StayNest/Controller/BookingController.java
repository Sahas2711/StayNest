package com.mit.StayNest.Controller;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.BookingRepository;
import com.mit.StayNest.Repository.UserRepository;
import com.mit.StayNest.Security.JwtHelper;
import com.mit.StayNest.Services.BookingService;
import com.mit.StayNest.Services.ListingService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ListingService listingService;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtHelper jwtHelper;

    // ✅ Utility: Extract user from JWT token
    private User getUserFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtHelper.getUsernameFromToken(token);

            logger.debug("Extracted email from token: {}", email);

            return userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        }

        logger.warn("Missing or invalid Authorization header");
        throw new RuntimeException("Authorization header is missing or invalid");
    }

    // ✅ Create a booking by authenticated user
    @PostMapping
    public Booking bookListing(@RequestBody Booking booking, HttpServletRequest request) {
        User user = getUserFromToken(request);
        booking.setTenant(user);

        logger.info("User {} is booking listing {}", user.getEmail(), booking.getListing().getId());
        return bookingService.createBooking(booking);
    }

    // ✅ Get bookings for the current user
    @GetMapping("/user/me")
    public List<Booking> getMyBookings(HttpServletRequest request) {
        User user = getUserFromToken(request);
        logger.info("Fetching bookings for user: {}", user.getEmail());
        return bookingRepo.findByTenant(user);
    }

    // ✅ Get bookings for a specific listing
    @GetMapping("/listing/{id}")
    public List<Booking> getBookingsForListing(@PathVariable Long id) {
        Listing listing = listingService.getSpecificListing(id);
        logger.info("Fetching bookings for listing ID: {}", id);
        return bookingService.getBookingsByListing(listing);
    }

    // ✅ Cancel a booking by ID
    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        logger.info("Cancelling booking ID: {}", id);
        return bookingService.cancelBooking(id);
    }

    // ✅ Get details of a specific booking
    @GetMapping("/{id}")
    public Booking getBookingDetails(@PathVariable Long id) {
        logger.info("Fetching booking details for ID: {}", id);
        return bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    // ✅ Get bookings by status
    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(@PathVariable String status) {
        logger.info("Fetching bookings with status: {}", status);
        return bookingService.getBookingsByStatus(status);
    }
}
