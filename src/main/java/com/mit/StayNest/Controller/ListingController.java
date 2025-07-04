package com.mit.StayNest.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Services.ListingServiceImpl;

@RestController
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    ListingServiceImpl listingServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<?> addListing(@RequestBody Listing listing) {
        try {
            Listing createdListing = listingServiceImpl.createListing(listing);
            return ResponseEntity.ok(createdListing);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


	
	@GetMapping("/{id}")
	public Listing getListingById(@PathVariable Long id) {
	    return this.listingServiceImpl.getSpecificListing(id);
	}

    @GetMapping("/all")
    public List<Listing> getAllListing() {
        return this.listingServiceImpl.getAllListing();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateListing(@RequestBody Listing listing) {
        try {
            Listing updated = listingServiceImpl.updateListing(listing);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteListing(@RequestBody Listing listing) {
        try {
            Listing deleted = listingServiceImpl.deleteListing(listing);
            return ResponseEntity.ok("Deleted listing with ID: " + deleted.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getListingByOwner(@PathVariable Long ownerId) {
        try {
            List<Listing> listings = listingServiceImpl.getListingByOwnerId(ownerId);
            return ResponseEntity.ok(listings);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/{area}")
    public List<Listing> searchByArea(@PathVariable String area) {
        return listingServiceImpl.searchByArea(area);
    }

}
