package com.codegym.controller;

import com.codegym.message.response.ResponseMessage;
import com.codegym.model.*;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.service.CategoryService;
import com.codegym.service.HouseService;
import com.codegym.service.ImageService;
import com.codegym.service.UserService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/guest")
public class GuestController {
  @Autowired
  UserService userService;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

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

  @GetMapping("/houses/search")
  public ResponseEntity<Iterable<House>> searchHouses(@QuerydslPredicate(root = House.class) Predicate predicate, HttpServletRequest request){
    Iterable<House> iterable  = houseService.findAll(predicate);
    return new ResponseEntity<Iterable<House>>(iterable, HttpStatus.OK);
  }

  @GetMapping("/houses")
  public ResponseEntity<List<House>> getAllHouse() {
    List<House> houses = houseService.findAll();
    return new ResponseEntity<List<House>>(houses, HttpStatus.OK);
  }

  @GetMapping("houses/{id}")
  public ResponseEntity getHouseDetail(@PathVariable Long id) {
    try {
      House house = houseService.findById(id);
      return new ResponseEntity<House>(house, HttpStatus.OK);
    } catch (EntityNotFoundException e){
      return new ResponseEntity(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
    }
  }
}






