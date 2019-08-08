package com.codegym.service;

import com.codegym.model.House;
import com.codegym.model.User;

import java.util.List;
import java.util.Optional;

public interface HouseService {
  House save(House house);

  Optional<House> findById(Long id);

  Boolean existsByHouseName(String houseName);

  House findByName(String name);

  List<House> findAll();

  List<House>findAllByUser(User user);

  void removeHouse(Long id);

}
