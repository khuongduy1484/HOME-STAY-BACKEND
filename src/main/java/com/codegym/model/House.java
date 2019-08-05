package com.codegym.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "house")
public class House {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
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
  private String describe;
  private Integer pricePerNight;

  @OneToMany(targetEntity = House.class)
  private Set<Image> listImages;

  @Enumerated(EnumType.STRING)
  private HouseStatus houseStatus;

  public HouseStatus getHouseStatus() {
    return houseStatus;
  }

  public void setHouseStatus(HouseStatus houseStatus) {
    this.houseStatus = houseStatus;
  }

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User owner;

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public House() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getPricePerNight() {
    return pricePerNight;
  }

  public void setPricePerNight(Integer pricePerNight) {
    this.pricePerNight = pricePerNight;
  }

  public House(@NotBlank @Size(min = 2, max = 50) String name, @NotBlank @Size(min = 2, max = 50) String address, Integer bedRooms, Integer bathRooms, @NotBlank @Size(min = 2, max = 50) String describe, Integer pricePerNight) {
    this.name = name;
    this.address = address;
    this.bedRooms = bedRooms;
    this.bathRooms = bathRooms;
    this.describe = describe;
    this.pricePerNight = pricePerNight;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }


  public Set<Image> getListImages() {
    return listImages;
  }

  public void setListImages(Set<Image> listImages) {
    this.listImages = listImages;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

}
