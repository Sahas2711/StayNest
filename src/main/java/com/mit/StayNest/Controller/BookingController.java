package com.mit.StayNest.Controller;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
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

    // ✅ Book a listing (POST /api/bookings)
    @PostMapping
    public Booking bookListing(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

//    // ✅ View my bookings (GET /api/bookings/user?tenantId=...)
//    @GetMapping("/user")
//    public List<Booking> getBookingsForTenant(@RequestParam Long tenantId) {
//        User tenant = userService.getUserById(tenantId);
//        return bookingService.getBookingsByTenant(tenant);
//    }
//
//    // ✅ View bookings for a listing (GET /api/bookings/listing/{id})
//    @GetMapping("/listing/{id}")
//    public List<Booking> getBookingsForListing(@PathVariable Long id) {
//        Listing listing = listingService.getListingById(id);
//        return bookingService.getBookingsByListing(listing);
//    }

    // ✅ Cancel a booking (PUT /api/bookings/{id}/cancel)
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

    // ✅ Filter bookings by status (GET /api/bookings/status/{status})
    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(@PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }
}
