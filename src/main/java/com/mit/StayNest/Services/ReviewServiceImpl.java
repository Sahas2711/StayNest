package com.mit.StayNest.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.StayNest.Entity.Review;
import com.mit.StayNest.Repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review updateReview(Review review) {
        if (!reviewRepository.existsById(review.getId())) {
            throw new RuntimeException("Review not found with ID: " + review.getId());
        }
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReviewById(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getReviewsByListingId(Long listingId) {
        return reviewRepository.findByListingId(listingId);
    }

    @Override
    public List<Review> getReviewsByTenantId(Long tenantId) {
        return reviewRepository.findByTenantId(tenantId);
    }
}
