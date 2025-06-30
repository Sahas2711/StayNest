package com.mit.StayNest.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pg_listings")
public class Listing {

    @Id
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; 

    private String title;

    private String address; 

    private Double rent; 

    public Listing() {
        super();
    }

    public Listing(Long id, User owner, String title, String address, Double rent) {
        super();
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.address = address;
        this.rent = rent;
    }

 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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


    @Override
    public String toString() {
        return "Listing [id=" + id 
                + ", title=" + title 
                + ", address=" + address 
                + ", rent=" + rent 
                + ", owner=" + (owner != null ? owner.getId() : null) + "]";
    }
}