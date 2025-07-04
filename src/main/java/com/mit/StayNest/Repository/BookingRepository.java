package com.mit.StayNest.Repository;

import com.mit.StayNest.Entity.Booking;
import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTenant(User tenant);
    List<Booking> findByListing(Listing listing);
    List<Booking> findByStatus(String status);
}
