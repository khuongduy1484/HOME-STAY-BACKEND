package com.codegym.service;

import com.codegym.model.User;
import com.codegym.model.UserBookedHouse;

import java.util.Date;
import java.util.List;

public interface UserBookedHouseService {
  UserBookedHouse save(UserBookedHouse userBookedHouse);

  List<UserBookedHouse> findByUserBook(User user);

  Boolean validateTime(UserBookedHouse userBookedHouse);

  void delete(UserBookedHouse userBookedHouse);

  UserBookedHouse findById(Long id);

  UserBookedHouse findByHouseName(String name);
}
