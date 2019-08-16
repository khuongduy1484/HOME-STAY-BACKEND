package com.codegym.controller;

import com.codegym.message.response.HouseInfo;
import com.codegym.model.*;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.MultipartFileService;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.service.CategoryService;
import com.codegym.service.HouseService;
import com.codegym.service.ImageService;
import com.codegym.service.UserService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/guest")
public class GuestController {
  @Autowired
  UserService userService;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private MultipartFileService multipartFileService;

  @Autowired
  JwtAuthTokenFilter authenticationJwtTokenFilter;
  @Autowired
  JwtProvider jwtProvider;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  HouseService houseService;
  @Autowired
  ImageService imageService;
  @Autowired
  CategoryService categoryService;

  @Value("${upload.location}")
  private String UPLOAD_LOCATION;

  @GetMapping("/houses/search")
  public ResponseEntity<List<HouseInfo>> searchHouses(@QuerydslPredicate(root = House.class) Predicate predicate){
    Iterable<House> iterable  = houseService.findAll(predicate);
    List<HouseInfo> housesInfo = new ArrayList<HouseInfo>();
    List<House> houses = StreamSupport.stream(iterable.spliterator(), false)
      .collect(Collectors.toList());
    houses .forEach(house -> housesInfo.add(convertHouseToHouseInfo(house)));
    return new ResponseEntity<List<HouseInfo>>(housesInfo, HttpStatus.OK);
  }

  @GetMapping("/houses")
  public ResponseEntity<List<HouseInfo>> getAllHouse() {
    List<House> houses = houseService.findAll();
    List<HouseInfo> housesInfo = new ArrayList<HouseInfo>();
    houses.forEach(house -> housesInfo.add(convertHouseToHouseInfo(house)));
    return new ResponseEntity<List<HouseInfo>>(housesInfo, HttpStatus.OK);
  }

  @GetMapping("houses/{id}")
  public ResponseEntity<HouseInfo> getHouseDetail(@PathVariable Long id) {
    HouseInfo houseInfo = convertHouseToHouseInfo(houseService.findById(id).orElseThrow(
      () -> new UsernameNotFoundException("House is not found : " )));
    return new ResponseEntity<HouseInfo>(houseInfo, HttpStatus.OK);
  }

  private HouseInfo convertHouseToHouseInfo(House house) {
    HouseInfo houseInfo = new HouseInfo();
    houseInfo.setId(house.getId());
    houseInfo.setName(house.getName());
    houseInfo.setAddress(house.getAddress());
    houseInfo.setBathRooms(house.getBathRooms());
    houseInfo.setBedRooms(house.getBedRooms());
    houseInfo.setPricePerNight(house.getPricePerNight());
    houseInfo.setDescription(house.getDescription());
    houseInfo.setCategory(house.getCategory().getName());
    houseInfo.setPricePerNight(house.getPricePerNight());
    houseInfo.setDescription(house.getDescription());
    houseInfo.setIsRented(house.getIsRented());
    User owner = house.getOwner();
    List<Image> images = imageService.findAllByHouse(house);
    List<String> imageFileName = new ArrayList<String>();
    images.forEach(image -> imageFileName.add(image.getFileName()));
    List<String> imagesSrc = new ArrayList<String>();
    imageFileName.forEach(fileName -> imagesSrc.add(findImageSrc(owner, house, fileName)));
    houseInfo.setImagesSrc(imagesSrc.toArray(new String[0]));
    houseInfo.setStatus(house.getStatus());
    return houseInfo;
  }

  private String findImageSrc(User owner, House house, String fileName) {
    String userName = owner.getUsername();
    String houseName = house.getName();
    return "resources/images/" + userName + "/" + houseName + "/" + fileName;
  }
}






