package com.mit.StayNest.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mit.StayNest.Entity.Listing;

public interface ListingRepository extends JpaRepository<Listing,Long>{
	Optional<Listing> findById(Long id);
	

}
