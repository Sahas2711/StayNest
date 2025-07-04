package com.mit.StayNest.Entity;


import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "bookings")
public class Booking {

    @Id 
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private User tenant; 

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing; 

    @Column(name = "start_date")
    private Date startDate; 

    @Column(name = "end_date")
    private Date endDate; 

    @Column
    private String status; // BOOKED or CANCELLED

    public Booking() {
        super();
    }

    public Booking(Long id, User tenant, Listing listing, Date startDate, Date endDate, String status) {
        super();
        this.id = id;
        this.tenant = tenant;
        this.listing = listing;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }


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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", tenant=" + (tenant != null ? tenant.getId() : null)
                + ", listing=" + (listing != null ? listing.getId() : null)
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", status=" + status + "]";
    }
}