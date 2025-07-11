
package com.mit.StayNest.Entity;



import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "reviews")
public class Review {
	@Id
	private Long id;
	@ManyToOne
	@JoinColumn(name = "tenant_id")
	private User tenant;
	@ManyToOne
	@JoinColumn(name = "listing_id")
	private Listing listing;
	private Long rating;
	private String feedback;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getTenant() {
		return tenant;
	}

	public void setTenant(User tenant) {
		this.tenant = tenant;
	}

	public Listing getListing() {
		return listing;
	}

	public void setListing(Listing listing) {
		this.listing = listing;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Review(Long id, User tenant, Listing listing, Long rating, String feedback, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.tenant = tenant;
		this.listing = listing;
		this.rating = rating;
		this.feedback = feedback;
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", rating=" + rating + ", feedback=" + feedback + ", createdAt=" + createdAt + "]";
	}

}