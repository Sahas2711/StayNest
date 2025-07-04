package com.mit.StayNest.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Repository.ListingRepository;
import com.mit.StayNest.Repository.UserRepository;

@Component
public class ListingServiceImpl implements ListingService {

	@Autowired
	ListingRepository listingRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Override
	public Listing createListing(Listing listing) {
	    try {
	        Long id = listing.getOwner().getId();
	        Optional<User> optionalUser = userRepo.findById(id);

	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();

	            if (!"OWNER".equalsIgnoreCase(user.getRole())) {
	                throw new RuntimeException("Unauthorized: Only owners can create listings.");
	            }

	            listing.setOwner(user); 
	            return listingRepo.save(listing);
	        } else {
	            throw new RuntimeException("User not found with ID: " + id);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error creating listing: " + e.getMessage());
	    }
	}

	@Override
	public List<Listing> getAllListing() {
		System.out.println("reached");
		return listingRepo.findAll();
	}

	@Override
	public Listing updateListing(Listing listing) {
		Optional<Listing> existingListing = listingRepo.findById(listing.getId());

		if (existingListing.isPresent()) {
			Listing updatedListing = existingListing.get();
			updatedListing.setTitle(listing.getTitle());
			updatedListing.setOwner(listing.getOwner());
			updatedListing.setRent(listing.getRent());
			updatedListing.setAddress(listing.getAddress());

			return listingRepo.save(updatedListing);
		} else {
			throw new RuntimeException("Listing not found with ID: " + listing.getId());
		}
	}

	@Override
	public Listing deleteListing(Listing listing) {
		Optional<Listing> existingListing = listingRepo.findById(listing.getId());

		if (existingListing.isPresent()) {
			listingRepo.deleteById(listing.getId());
			return existingListing.get();
		} else {
			throw new RuntimeException("Listing Not found with ID :- " + listing.getId());
		}
	}
	
	@Override
	public List<Listing> getListingByOwnerId(long ownerId){
		Optional<User> optionalUser = userRepo.findById(ownerId);
		if(optionalUser.isPresent()) {
			return listingRepo.findByOwnerId(ownerId);
		}
		else {
			throw new RuntimeException("Owner not found with ID : "+ownerId);
		}
		
	}
	

	@Override
	public List<Listing> searchByArea(String area) {
		return listingRepo.findByAddressContaining(area);
		
	}

	@Override
	public Listing getSpecificListing(Long id) {
		 Optional<Listing> listing = listingRepo.findById(id);
		    if (listing.isPresent()) {
		        return listing.get();
		    } else {
		        throw new RuntimeException("No Listing exists with this id: " + id);
		    }
	}

}
