package com.codegym.message.response;

import com.codegym.model.HouseStatus;
import org.hibernate.validator.constraints.NotBlank;


import javax.validation.constraints.Size;
import java.util.List;

public class HouseInfo {

  private Long id;

  @NotBlank
  @Size(min = 2, max = 50)
  private String name;

  @NotBlank
  @Size(min = 2)
  private String address;

  private Integer bedRooms;

  private Integer bathRooms;

  @NotBlank
  @Size(min = 2, max = 50)
  private String description;

  private Integer pricePerNight;

  private String category;

  private String[] imagesSrc;

  private HouseStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String[] getImagesSrc() {
    return imagesSrc;
  }

  public void setImagesSrc(String[] imagesSrc) {
    this.imagesSrc = imagesSrc;
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getPricePerNight() {
    return pricePerNight;
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

  public void setPricePerNight(Integer pricePerNight) {
    this.pricePerNight = pricePerNight;
  }

  public HouseStatus getStatus() {
    return status;
  }

  public void setStatus(HouseStatus status) {
    this.status = status;
  }
}
