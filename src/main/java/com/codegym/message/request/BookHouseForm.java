package com.codegym.message.request;

import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

public class BookHouseForm {
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date checkIn;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date checkOut;


  public Date getCheckIn() {
    return checkIn;
  }

  public void setCheckIn(Date checkIn) {
    this.checkIn = checkIn;
  }


  public Date getCheckOut() {
    return checkOut;
  }

  public void setCheckOut(Date checkOut) {
    this.checkOut = checkOut;
  }

  public BookHouseForm() {
  }
}
