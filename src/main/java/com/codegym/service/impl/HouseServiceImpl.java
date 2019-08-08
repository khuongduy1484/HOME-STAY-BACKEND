package com.codegym.service.impl;

import com.codegym.model.House;
import com.codegym.model.User;
import com.codegym.repository.HouseRepository;
import com.codegym.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HouseServiceImpl implements HouseService {
  @Autowired
  HouseRepository houseRepository;

  @Override
  public House save(House house) {
    return houseRepository.save(house);
  }

  @Override
  public Optional<House> findById(Long id) {
    return houseRepository.findById(id);
  }

  @Override
  public Boolean existsByHouseName(String houseName) {
    return houseRepository.existsByName(houseName);
  }

  @Override
  public House findByName(String name) {
    return houseRepository.findByName(name);
  }

  @Override
  public List<House> findAll() {
    return houseRepository.findAll();
  }

  @Override
  public List<House> findAllByUser(User user) {
    return houseRepository.findAllByOwner(user);
  }

  @Override
  public void removeHouse(Long id) {
    houseRepository.deleteById(id);
  }




}
