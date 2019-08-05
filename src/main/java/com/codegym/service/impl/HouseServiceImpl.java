package com.codegym.service.impl;

import com.codegym.model.House;
import com.codegym.repository.HouseRepository;
import com.codegym.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl implements HouseService {
  @Autowired
  HouseRepository houseRepository;

  @Override
  public House save(House house) {
    return houseRepository.save(house);
  }

  @Override
  public House findById(Long id) {
    return houseRepository.findById(id).get();
  }

  @Override
  public Boolean existsByHouseName(String houseName) {
    return houseRepository.existsByName(houseName);
  }

  @Override
  public House findByName(String name) {
    return houseRepository.findByName(name);
  }


}
