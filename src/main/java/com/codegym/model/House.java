package com.codegym.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "house")
public class House {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 2, max = 50)
  @Column(unique = true)
  private String name;
  @NotBlank
  @Size(min = 2)
  private String address;
  private Integer bedRooms;

  private Integer bathRooms;

  @NotBlank
  @Size(min = 2)
  @Column(length = 2000)
  private String description;

  private Integer pricePerNight;

  @OneToMany(targetEntity = Image.class,cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
//  @JoinTable(name = "house_image",
//    joinColumns = @JoinColumn(name = "house_id"),
//    inverseJoinColumns = @JoinColumn(name = "image_id"))
  private List<Image> images;

  private Boolean isRented;

  @Enumerated(EnumType.STRING)
  private HouseStatus status;

  public House(@NotBlank @Size(min = 2, max = 50) String name, @NotBlank @Size(min = 2) String address, Integer bedRooms, Integer bathRooms, @NotBlank @Size(min = 2) String description, Integer pricePerNight, List<Image> images, Boolean isRented, HouseStatus status, Category category, User owner) {
    this.name = name;
    this.address = address;
    this.bedRooms = bedRooms;
    this.bathRooms = bathRooms;
    this.description = description;
    this.pricePerNight = pricePerNight;
    this.images = images;
    this.isRented = isRented;
    this.status = status;
    this.category = category;
    this.owner = owner;
  }

  public HouseStatus getHouseStatus() {
    return status;
  }

  public void setHouseStatus(HouseStatus status) {
    this.status = status;
  }

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User owner;

  public Boolean getIsRented() {
    return isRented;
  }

  public void setIsRented(Boolean rented) {
    isRented = rented;
  }

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

  public House(@NotBlank @Size(min = 2, max = 50) String name, @NotBlank @Size(min = 2) String address, Integer bedRooms, Integer bathRooms, @NotBlank @Size(min = 2, max = 50) String description, Integer pricePerNight) {
    this.name = name;
    this.address = address;
    this.bedRooms = bedRooms;
    this.bathRooms = bathRooms;
    this.description = description;
    this.pricePerNight = pricePerNight;
    this.status = HouseStatus.AVAILABLE;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public HouseStatus getStatus() {
    return status;
  }

  public void setStatus(HouseStatus status) {
    this.status = status;
  }
}
