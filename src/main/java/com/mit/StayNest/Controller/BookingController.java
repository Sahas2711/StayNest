package com.mit.StayNest.Controller;

import com.mit.StayNest.Entity.Booking;

import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.BookingRepository;
import com.mit.StayNest.Repository.UserRepository;
import com.mit.StayNest.Services.BookingService;
import com.mit.StayNest.Services.ListingService;
import com.mit.StayNest.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ListingService listingService;

    @Autowired
    BookingRepository bookingRepo;
    
    @Autowired
    private UserRepository userRepo; 
    
    // Book a listing (POST /api/bookings)
    @PostMapping
    public Booking bookListing(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/user/{id}")
    public List<Booking> getBookingsForTenant(@PathVariable("id") Long tenantId) {
        User tenant = userRepo.findById(tenantId)
                              .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepo.findByTenant(tenant);
    }
    // View bookings for a listing 
    @GetMapping("/listing/{id}")
    public List<Booking> getBookingsForListing(@PathVariable Long id) {
        Listing listing = listingService.getSpecificListing(id);
        return bookingService.getBookingsByListing(listing);
    }



    // Cancel a booking 
    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    // ✅ Get booking details (GET /api/bookings/{id})
    @GetMapping("/{id}")
    public Booking getBookingDetails(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    //  Filter bookings by status
    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(@PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }
}
