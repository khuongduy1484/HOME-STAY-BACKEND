package com.codegym.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_booked_house")
public class UserBookedHouse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "house_id", nullable = false)
  private House house;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "book_date")
  private Date bookDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "checkin_date")
  private Date checkinDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "checkout_date")
  private Date checkoutDate;

  public UserBookedHouse() {
  }

  public UserBookedHouse(User user, House house, Date bookDate, Date checkinDate, Date checkoutDate) {
    this.user = user;
    this.house = house;
    this.bookDate = bookDate;
    this.checkinDate = checkinDate;
    this.checkoutDate = checkoutDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public House getHouse() {
    return house;
  }

  public void setHouse(House house) {
    this.house = house;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getBookDate() {
    return bookDate;
  }

  public void setBookDate(Date bookDate) {
    this.bookDate = bookDate;
  }

  public Date getCheckinDate() {
    return checkinDate;
  }

  public void setCheckinDate(Date checkinDate) {
    this.checkinDate = checkinDate;
  }

  public Date getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(Date checkoutDate) {
    this.checkoutDate = checkoutDate;
  }
}
