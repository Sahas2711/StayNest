package com.mit.StayNest.Services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mit.StayNest.Entity.Listing;
import com.mit.StayNest.Entity.User;
import com.mit.StayNest.Entity.Owner;
import com.mit.StayNest.Repository.ListingRepository;
import com.mit.StayNest.Repository.OwnerRepository;
import com.mit.StayNest.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class ListingServiceImpl implements ListingService {
	private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

	@Autowired
	ListingRepository listingRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	OwnerRepository ownerRepo;

	@Override
	public Listing createListing(Listing listing) {
		Long id = listing.getOwner().getId();
		 logger.info("Creating listing for owner ID: {}", id);
		try {
			
			Optional<Owner> optionalUser = ownerRepo.findById(id);

			if (optionalUser.isPresent()) {
				Owner user = optionalUser.get();
				 logger.info("Owner found. Saving listing...");
				listing.setOwner(user);
				return listingRepo.save(listing);
			} else {
				logger.warn("Owner not found with ID: {}", id);
				throw new RuntimeException("Owner not found with ID: " + id);
			}
		} catch (Exception e) {
			 logger.error("Error creating listing for owner ID: {} - {}", id, e.getMessage(), e);
			throw new RuntimeException("Error creating listing: " + e.getMessage());
		}
	}

	@Override
	public List<Listing> getAllListing() {
		logger.info("Fetching all listings");
		return listingRepo.findAll();
	}

	@Override
	public Listing updateListing(Listing listing) {
		logger.info("Updating listing with ID: {}", listing.getId());
		Optional<Listing> existingListing = listingRepo.findById(listing.getId());

		if (existingListing.isPresent()) {
			Listing updatedListing = existingListing.get();
			updatedListing.setTitle(listing.getTitle());
			updatedListing.setOwner(listing.getOwner());
			updatedListing.setRent(listing.getRent());
			updatedListing.setAddress(listing.getAddress());
			logger.info("Listing updated successfully with ID: {}", updatedListing.getId());
			return listingRepo.save(updatedListing);
		} else {
			logger.warn("Listing not found for update with ID: {}", listing.getId());
			throw new RuntimeException("Listing not found with ID: " + listing.getId());
		}
	}

	@Override
	public Listing deleteListing(Listing listing) {
		logger.info("Attempting to delete listing with ID: {}", listing.getId());
		Optional<Listing> existingListing = listingRepo.findById(listing.getId());

		if (existingListing.isPresent()) {
			listingRepo.deleteById(listing.getId());
			logger.info("Listing deleted successfully with ID: {}", listing.getId());
			return existingListing.get();
		} else {
			 logger.warn("Listing not found for deletion with ID: {}", listing.getId());
			throw new RuntimeException("Listing Not found with ID :- " + listing.getId());
		}
	}

	@Override
	public List<Listing> getListingByOwnerId(long ownerId) {
		 logger.info("Fetching listings for owner ID: {}", ownerId);
		Optional<User> optionalUser = userRepo.findById(ownerId);
		if (optionalUser.isPresent()) {
			return listingRepo.findByOwnerId(ownerId);
		} else {
			 logger.warn("Owner not found with ID: {}", ownerId);
			throw new RuntimeException("Owner not found with ID : " + ownerId);
		}

	}

	@Override
	public List<Listing> searchByArea(String area) {
		logger.info("Searching listings in area containing: '{}'", area);
		return listingRepo.findByAddressContaining(area);

	}

	@Override
	public Listing getSpecificListing(Long id) {
		logger.info("Fetching specific listing with ID: {}", id);
		Optional<Listing> listing = listingRepo.findById(id);
		if (listing.isPresent()) {
			return listing.get();
		} else {
			logger.warn("Listing not found with ID: {}", id);
			throw new RuntimeException("No Listing exists with this id: " + id);
		}
	}
	public Optional<Listing> findById(Long id) {
		logger.info("Finding listing by ID: {}", id);
	    return listingRepo.findById(id);
	}


}
