package com.codegym.service;

import com.codegym.model.House;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface HouseService {
  House save(House house);

  House findById(Long id);

  Boolean existsByHouseName(String houseName);

  House findByName(String name);

  List<House> findAll();

  Iterable<House> findAll(Predicate predicate);

  List<House> findAllByBathRooms(Integer bathRooms);

  List<House> findAllByBedRooms(Integer bedRooms);

  List<House> findAllByPricePerNightBetween(Integer minPrice, Integer maxPrice);
}
