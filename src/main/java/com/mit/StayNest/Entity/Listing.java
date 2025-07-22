package com.mit.StayNest.Entity;

import java.util.Date;

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
	private String gender;
	// Facilities
	@Column(nullable = false)
	private boolean isWifiAvilable;
	@Column(nullable = false)
	private boolean isAcAvilable;
	@Column(nullable = false)
	private boolean isMealsAvilable;
	@Column(nullable = false)
	private boolean isLaudryAvilable;
	@Column(nullable = false)
	private boolean isCctvAvilable;
	@Column(nullable = false)
	private boolean isParkingAvilable;
	@Column(nullable = false)
	private boolean isCommonAreasAvilable;
	@Column(nullable = false)
	private boolean isStudyDeskAvilable;

	@Column(nullable = false)
	private Double rent;

	@Column(nullable = false)
	private Double deposite;

	@Column(nullable = false)
	private Double discount;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false, length = 1000)
	private String description;

	@Column(nullable = false)
	private String roomType;

	@Column(nullable = false)
	private Date startDate;

	
	private Date endDate;

	@CreationTimestamp
	private Date createdAt;

	@UpdateTimestamp
	private Date updatedAt;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getRent() {
		return rent;
	}

	public void setRent(Double rent) {
		this.rent = rent;
	}

	public Double getDeposite() {
		return deposite;
	}

	public void setDeposite(Double deposite) {
		this.deposite = deposite;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public boolean getIsWifiAvilable() {
		return isWifiAvilable;
	}

	public void setWifiAvilable(boolean isWifiAvilable) {
		this.isWifiAvilable = isWifiAvilable;
	}

	public boolean getIsAcAvilable() {
		return isAcAvilable;
	}

	public void setAcAvilable(boolean isAcAvilable) {
		this.isAcAvilable = isAcAvilable;
	}

	public boolean getIsMealsAvilable() {
		return isMealsAvilable;
	}

	public void setMealsAvilable(boolean isMealsAvilable) {
		this.isMealsAvilable = isMealsAvilable;
	}

	public boolean getIsLaudryAvilable() {
		return isLaudryAvilable;
	}

	public void setLaudryAvilable(boolean isLaudryAvilable) {
		this.isLaudryAvilable = isLaudryAvilable;
	}

	public boolean getIsCctvAvilable() {
		return isCctvAvilable;
	}

	public void setCctvAvilable(boolean isCctvAvilable) {
		this.isCctvAvilable = isCctvAvilable;
	}

	public boolean getIsParkingAvilable() {
		return isParkingAvilable;
	}

	public void setParkingAvilable(boolean isParkingAvilable) {
		this.isParkingAvilable = isParkingAvilable;
	}

	public boolean getIsCommonAreasAvilable() {
		return isCommonAreasAvilable;
	}

	public void setCommonAreasAvilable(boolean isCommonAreasAvilable) {
		this.isCommonAreasAvilable = isCommonAreasAvilable;
	}

	public boolean getIsStudyDeskAvilable() {
		return isStudyDeskAvilable;
	}

	public void setStudyDeskAvilable(boolean isStudyDeskAvilable) {
		this.isStudyDeskAvilable = isStudyDeskAvilable;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Listing() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Listing(Long id, Owner owner, String title, String address, String gender, boolean isWifiAvilable,
			boolean isAcAvilable, boolean isMealsAvilable, boolean isLaudryAvilable, boolean isCctvAvilable,
			boolean isParkingAvilable, boolean isCommonAreasAvilable, boolean isStudyDeskAvilable, Double rent,
			Double deposite, Double discount, String url, String description, String roomType, Date startDate,
			Date endDate, Date createdAt, Date updatedAt) {
		super();
		this.id = id;
		this.owner = owner;
		this.title = title;
		this.address = address;
		this.gender = gender;
		this.isWifiAvilable = isWifiAvilable;
		this.isAcAvilable = isAcAvilable;
		this.isMealsAvilable = isMealsAvilable;
		this.isLaudryAvilable = isLaudryAvilable;
		this.isCctvAvilable = isCctvAvilable;
		this.isParkingAvilable = isParkingAvilable;
		this.isCommonAreasAvilable = isCommonAreasAvilable;
		this.isStudyDeskAvilable = isStudyDeskAvilable;
		this.rent = rent;
		this.deposite = deposite;
		this.discount = discount;
		this.url = url;
		this.description = description;
		this.roomType = roomType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Listing [id=" + id + ", owner=" + owner + ", title=" + title + ", address=" + address + ", gender="
				+ gender + ", isWifiAvilable=" + isWifiAvilable + ", isAcAvilable=" + isAcAvilable
				+ ", isMealsAvilable=" + isMealsAvilable + ", isLaudryAvilable=" + isLaudryAvilable
				+ ", isCctvAvilable=" + isCctvAvilable + ", isParkingAvilable=" + isParkingAvilable
				+ ", isCommonAreasAvilable=" + isCommonAreasAvilable + ", isStudyDeskAvilable=" + isStudyDeskAvilable
				+ ", rent=" + rent + ", deposite=" + deposite + ", discount=" + discount + ", url=" + url
				+ ", description=" + description + ", roomType=" + roomType + ", startDate=" + startDate + ", endDate="
				+ endDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
