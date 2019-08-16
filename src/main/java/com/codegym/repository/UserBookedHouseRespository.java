package com.codegym.repository;

import com.codegym.model.User;
import com.codegym.model.UserBookedHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookedHouseRespository extends JpaRepository<UserBookedHouse,Long> {
  List<UserBookedHouse> findAllByUser(User user);
  UserBookedHouse findByHouseName(String name);
}
