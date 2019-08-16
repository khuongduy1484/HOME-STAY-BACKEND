package com.codegym.controller;

import com.codegym.message.request.PublishHouseForm;
import com.codegym.message.request.UpdateInfoHouseForm;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.Category;
import com.codegym.model.House;
import com.codegym.model.Image;
import com.codegym.model.User;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.MultipartFileService;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.service.CategoryService;
import com.codegym.service.HouseService;
import com.codegym.service.ImageService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/owner")
public class OwnerController {
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

  @PostMapping(value = "/houses", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> createHouse(@ModelAttribute PublishHouseForm publishHouseForm, HttpServletRequest request) {
    String jwt = authenticationJwtTokenFilter.getJwt(request);
    String userName = jwtProvider.getUserNameFromJwtToken(jwt);
    User user;
    try {
      user = userService.findByUsername(userName).orElseThrow(
        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + userName));
    }
    catch (UsernameNotFoundException exception) {
      return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    House house = new House(publishHouseForm.getName(), publishHouseForm.getAddress(), publishHouseForm.getBedRooms(),
      publishHouseForm.getBathRooms(), publishHouseForm.getDescription(), publishHouseForm.getPricePerNight());
    house.setOwner(user);
    Category categoryName = categoryService.findByCategoryName(publishHouseForm.getCategory());
    house.setCategory(categoryName);
    houseService.save(house);
    House houseName = houseService.findByName(publishHouseForm.getName());
    MultipartFile[] gallery = publishHouseForm.getImages();
    Set<Image> houseImages = new HashSet<>();
    multipartFileService.saveGallery(userName, houseName, gallery, houseImages, UPLOAD_LOCATION);

    return new ResponseEntity<>(new ResponseMessage("Publish House successfully"), HttpStatus.OK);
  }

  @GetMapping("list-house")
  public ResponseEntity<List<House>> listHouse() {
    List<House> houses = houseService.findAll();
    if (houses.isEmpty()) {
      return new ResponseEntity<List<House>>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<House>>(houses, HttpStatus.OK);
  }
  @GetMapping("/house")
  public ResponseEntity<List<House>> listHouseByUser() {
    User user = userService.getUserByAuth();
    List<House> houses = houseService.findAllByUser(user);
    if (houses.isEmpty()) {
      return new ResponseEntity<List<House>>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<House>>(houses, HttpStatus.OK);
  }
  @GetMapping("/{id}")
  public ResponseEntity<House> getHouse(@PathVariable("id") Long id) {
    House house = houseService.findById(id).orElseThrow(
      () -> new UsernameNotFoundException("House is not found : " ));
    return new ResponseEntity<House>(house,HttpStatus.OK);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteHouse(@PathVariable("id") Long id) {
    House house = houseService.findById(id).orElseThrow(
      () -> new UsernameNotFoundException("House is not found : " ));
    if (house.getIsRented()) {
      return new ResponseEntity<>(new ResponseMessage("cannot delete house"), HttpStatus.BAD_REQUEST);
    }
    houseService.removeHouse(id);
    return new ResponseEntity<>(new ResponseMessage("Remove House successfully"), HttpStatus.OK);
  }
  @PutMapping(value = "/{id}",consumes = "multipart/form-data")
  public ResponseEntity<?> editHouse(@PathVariable("id") Long id, @ModelAttribute UpdateInfoHouseForm updateInfoHouseForm) {
    User user = userService.getUserByAuth();
    House house = houseService.findById(id).orElseThrow(
      () -> new UsernameNotFoundException("House is not found : " ));
    house.setName(updateInfoHouseForm.getName());
    house.setAddress(updateInfoHouseForm.getAddress());
    house.setBedRooms(updateInfoHouseForm.getBedRooms());
    house.setBathRooms(updateInfoHouseForm.getBathRooms());
    house.setIsRented(updateInfoHouseForm.getIsRented());
    Category category = categoryService.findByCategoryName(updateInfoHouseForm.getCategory());
    house.setCategory(category);
    house.setDescription(updateInfoHouseForm.getDescription());
    house.setPricePerNight(updateInfoHouseForm.getPricePerNight());
    MultipartFile[] gallery = updateInfoHouseForm.getImages();
    Set<Image> images = new HashSet<>();
    multipartFileService.saveGallery(user.getUsername(),house,gallery,images,UPLOAD_LOCATION);
    houseService.save(house);
    return new ResponseEntity<>(new ResponseMessage("Update House successfully"), HttpStatus.OK);
  }
}
