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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/owner")
public class OwnerController {
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

  @PostMapping(value = "/houses", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> createHouse(@ModelAttribute House house, HttpServletRequest request) {
    User user = userService.getUserByAuth();
    house.setOwner(user);
    house.setStatus(HouseStatus.AVAILABLE);
    house.setIsRented(false);
    houseService.save(house);
    return new ResponseEntity<>(new ResponseMessage("Publish House successfully"), HttpStatus.OK);
  }

  @GetMapping("list-house")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<List<House>> listHouse() {
    List<House> houses = houseService.findAll();
    if (houses.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(houses, HttpStatus.OK);
  }
  @GetMapping("/house")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<List<House>> listHouseByUser() {
    User user = userService.getUserByAuth();
    List<House> houses = houseService.findAllByUser(user);
    if (houses.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(houses, HttpStatus.OK);
  }
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<House> getHouse(@PathVariable("id") Long id) {
    try {
      House house = houseService.findById(id);
      return new ResponseEntity<House>(house,HttpStatus.OK);
    }catch (EntityNotFoundException e){
      return new ResponseEntity(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
    }
  }
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> deleteHouse(@PathVariable("id") Long id) {
    try {
      House house = houseService.findById(id);
      if (house.getIsRented()) {
        return new ResponseEntity<>(new ResponseMessage("cannot delete house"), HttpStatus.BAD_REQUEST);
      }
      houseService.removeHouse(id);
      return new ResponseEntity<>(new ResponseMessage("Remove House successfully"), HttpStatus.OK);
    } catch (EntityNotFoundException e){
      return new ResponseEntity(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
    }
  }
  @PutMapping(value = "/{id}",consumes = "multipart/form-data")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> editHouse(@PathVariable("id") Long id, @ModelAttribute House house) {
    try {
      User user = userService.getUserByAuth();
      User owner = houseService.findById(id).getOwner();
      if (user.getId().equals(owner.getId())){
        house.setOwner(user);
        houseService.save(house);
        return new ResponseEntity<>(new ResponseMessage("Update House successfully"), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ResponseMessage("You are not owner of this house"),HttpStatus.FORBIDDEN);
    } catch (EntityNotFoundException e){
      return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping("/categories")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<List<Category>> getCategories(){
    return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
  }
}
