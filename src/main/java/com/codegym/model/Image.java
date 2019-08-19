package com.codegym.model;

import javax.persistence.*;

@Entity
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String imageUrl;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "house_id",referencedColumnName = "id")
  private House house;

  public Image() {
  }

  public Image(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public House getHouse() {
    return house;
  }

  public void setHouse(House house) {
    this.house = house;
  }
}
