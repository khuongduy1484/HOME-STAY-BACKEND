package com.codegym.service;

import com.codegym.model.House;

import java.util.List;

public interface HouseService {
  House save(House house);

  House findById(Long id);

  Boolean existsByHouseName(String houseName);

  House findByName(String name);
  public List<House> findAll();
}
