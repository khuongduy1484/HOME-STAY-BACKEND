package com.demo.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "house")
public class House {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  @Size(min = 2,max = 50)
  private String name;
  private String address;
  @Column(name = "bed-rooms")
  private String bedRooms;
  @Column(name = "bathRooms")
  private String bathRooms;
  private String describe;
  @Column(name = "pricePerNight")
  private String pricePerNight;
  @Column(name = "living-rooms")
  private String livingRoom;

  public String getLivingRoom() {
    return livingRoom;
  }

  public void setLivingRoom(String livingRoom) {
    this.livingRoom = livingRoom;
  }

  public String getKitchen() {
    return kitchen;
  }

  public void setKitchen(String kitchen) {
    this.kitchen = kitchen;
  }

  private String kitchen;
  @OneToMany
  private Set<Image> images;
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;


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

  public String getBedRooms() {
    return bedRooms;
  }

  public void setBedRooms(String bedRooms) {
    this.bedRooms = bedRooms;
  }

  public String getBathRooms() {
    return bathRooms;
  }

  public void setBathRooms(String bathRooms) {
    this.bathRooms = bathRooms;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

  public String getPricePerNight() {
    return pricePerNight;
  }

  public void setPricePerNight(String pricePerNight) {
    this.pricePerNight = pricePerNight;
  }

  public Set<Image> getImages() {
    return images;
  }

  public void setImages(Set<Image> images) {
    this.images = images;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

}
