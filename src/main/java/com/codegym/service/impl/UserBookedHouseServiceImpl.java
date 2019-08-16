package com.codegym.service.impl;

import com.codegym.model.User;
import com.codegym.model.UserBookedHouse;
import com.codegym.repository.UserBookedHouseRespository;
import com.codegym.service.UserBookedHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

@Service
public class UserBookedHouseServiceImpl implements UserBookedHouseService {
  @Autowired
  UserBookedHouseRespository userBookedHouseRespository;

  @Override
  public UserBookedHouse save(UserBookedHouse userBookedHouse) {
    return userBookedHouseRespository.save(userBookedHouse);
  }


  @Override
  public List<UserBookedHouse> findByUserBook(User user) {
    return userBookedHouseRespository.findAllByUser(user);
  }

  @Override
  public Boolean validateTime(UserBookedHouse userBookedHouse) {
    Calendar aDayBeforeCheckin = Calendar.getInstance();
    aDayBeforeCheckin.setTime(userBookedHouse.getCheckinDate());
    aDayBeforeCheckin.add(Calendar.DATE,-1);
    Date nowDate = new Date();
    Calendar aDayNow = Calendar.getInstance();
    aDayNow.setTime(nowDate);
    aDayNow.add(Calendar.DATE,0);
    return aDayNow.before(aDayBeforeCheckin);
  }

  @Override
  public void delete(UserBookedHouse userBookedHouse) {
    userBookedHouseRespository.delete(userBookedHouse);
  }

  @Override
  public UserBookedHouse findById(Long id) {
    return userBookedHouseRespository.findById(id).get();
  }

  @Override
  public UserBookedHouse findByHouseName(String name) {
    return userBookedHouseRespository.findByHouseName(name);
  }
}
