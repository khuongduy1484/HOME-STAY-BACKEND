package com.codegym.message.request;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

public class UpdateInfoHouseForm {
  @NotBlank
  @Size(min = 2, max = 50)
  private String name;
  @NotBlank
  @Size(min = 2, max = 50)
  private String address;
  private Integer bedRooms;
  private Integer bathRooms;
  @NotBlank
  @Size(min = 2, max = 50)
  private String description;
  private Integer pricePerNight;
  private String category;
  private Boolean isRented;
  private MultipartFile[] images;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getBedRooms() {
    return bedRooms;
  }

  public void setBedRooms(Integer bedRooms) {
    this.bedRooms = bedRooms;
  }

  public Integer getBathRooms() {
    return bathRooms;
  }

  public void setBathRooms(Integer bathRooms) {
    this.bathRooms = bathRooms;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getPricePerNight() {
    return pricePerNight;
  }

  public void setPricePerNight(Integer pricePerNight) {
    this.pricePerNight = pricePerNight;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Boolean getIsRented() {
    return isRented;
  }

  public void setIsRented(Boolean rented) {
    isRented = rented;
  }

  public MultipartFile[] getImages() {
    return images;
  }

  public void setImages(MultipartFile[] images) {
    this.images = images;
  }


}
