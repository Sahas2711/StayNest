package com.mit.StayNest.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "pg_listings")
public class Listing {


    @Id
    private Long id;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double rent;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, length = 1000)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // === Constructors ===
    public Listing() {
        super();
    }

    public Listing(Long id, Owner owner, String title, String address, Double rent, String url, String description) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.address = address;
        this.rent = rent;
        this.url = url;
        this.description = description;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

	@Override
	public String toString() {
		return "Listing [id=" + id + ", owner=" + owner + ", title=" + title + ", address=" + address + ", rent=" + rent
				+ ", url=" + url + ", description=" + description + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}
