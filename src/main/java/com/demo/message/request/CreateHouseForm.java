package com.demo.message.request;

import com.demo.model.Category;
import com.demo.model.Image;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.Set;

public class CreateHouseForm {
  @NotBlank
  @Size(min = 2, max = 50)
  private String name;
  @NotBlank
  @Size(min = 2,max = 50)
  private String address;
  private Integer bedRooms;
  private Integer bathRooms;
  @NotBlank
  @Size(min = 2,max = 50)
  private String describe;
  private Integer pricePerNight;
  private Category category;
  private MultipartFile[] images;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public MultipartFile[] getImages() {
    return images;
  }

  public void setImages(MultipartFile[] images) {
    this.images = images;
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




  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
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

  public CreateHouseForm(@NotBlank @Size(min = 2, max = 50) String name, @NotBlank @Size(min = 2, max = 50) String address, Integer bedRooms, Integer bathRooms, @NotBlank @Size(min = 2, max = 50) String describe, Integer pricePerNight, Category category, MultipartFile[] images) {
    this.name = name;
    this.address = address;
    this.bedRooms = bedRooms;
    this.bathRooms = bathRooms;
    this.describe = describe;
    this.pricePerNight = pricePerNight;
    this.category = category;
    this.images = images;
  }
}
