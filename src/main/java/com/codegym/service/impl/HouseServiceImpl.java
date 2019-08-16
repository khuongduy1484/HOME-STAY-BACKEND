package com.codegym.service.impl;

import com.codegym.model.House;
import com.codegym.repository.HouseRepository;
import com.codegym.service.HouseService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  public List<House> findAll(){ return houseRepository.findAll();}

  @Override
  public Iterable<House> findAll(Predicate predicate) {
    return houseRepository.findAll(predicate);
  }

  @Override
  public List<House> findAllByBathRooms(Integer bathRooms) {
    return houseRepository.findAllByBathRooms(bathRooms);
  }

  @Override
  public List<House> findAllByBedRooms(Integer bedRooms) {
    return houseRepository.findAllByBedRooms(bedRooms);
  }

  @Override
  public List<House> findAllByPricePerNightBetween(Integer minPrice, Integer maxPrice) {
    return houseRepository.findAllByPricePerNightBetween(minPrice, maxPrice);
  }
}
