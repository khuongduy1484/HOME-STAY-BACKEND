package com.codegym.service;

import com.codegym.model.House;
import com.codegym.model.User;
import com.querydsl.core.types.Predicate;

import java.util.List;
import java.util.Optional;

public interface HouseService {
  House save(House house);

  Optional<House> findById(Long id);

  Boolean existsByHouseName(String houseName);

  House findByName(String name);
  List<House>findAllByUser(User user);
  List<House> findAll();

  Iterable<House> findAll(Predicate predicate);

  List<House> findAllByBathRooms(Integer bathRooms);

  List<House> findAllByBedRooms(Integer bedRooms);

  List<House> findAllByPricePerNightBetween(Integer minPrice, Integer maxPrice);
  void removeHouse(Long id);
}
