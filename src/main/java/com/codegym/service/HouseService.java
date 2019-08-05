package com.codegym.service;

import com.codegym.model.House;

public interface HouseService {
  House save(House house);

  House findById(Long id);

  Boolean existsByHouseName(String houseName);

  House findByName(String name);
}
