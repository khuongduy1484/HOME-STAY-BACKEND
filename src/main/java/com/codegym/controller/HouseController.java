package com.codegym.controller;

import com.codegym.message.request.PublishHouseForm;
import com.codegym.message.response.HouseInfo;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.*;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.MultipartFileService;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.service.UserService;
import com.codegym.service.impl.CategoryServiceImpl;
import com.codegym.service.impl.HouseServiceImpl;
import com.codegym.service.impl.ImageServiceImpl;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/houses")
public class HouseController {
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
  HouseServiceImpl houseService;
  @Autowired
  ImageServiceImpl imageService;
  @Autowired
  CategoryServiceImpl categoryService;

  @Value("${upload.location}")
  private String UPLOAD_LOCATION;

  @PostMapping(consumes = "multipart/form-data")
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

  @GetMapping
  public ResponseEntity<List<HouseInfo>> getAllHouse() {
    List<House> houses = houseService.findAll();
    List<HouseInfo> housesInfo = new ArrayList<HouseInfo>();
    houses.forEach(house -> housesInfo.add(convertHouseToHouseInfo(house)));
    return new ResponseEntity<List<HouseInfo>>(housesInfo,HttpStatus.OK);
  }

  private HouseInfo convertHouseToHouseInfo(House house) {
    HouseInfo houseInfo = new HouseInfo();
    houseInfo.setName(house.getName());
    houseInfo.setAddress(house.getAddress());
    houseInfo.setBathRooms(house.getBathRooms());
    houseInfo.setBedRooms(house.getBedRooms());
    houseInfo.setCategory(house.getCategory().getCategoryName());
    houseInfo.setPricePerNight(house.getPricePerNight());
    houseInfo.setDescription(house.getDescription());
    User owner = house.getOwner();
    List<Image> images = imageService.findAllByHouse(house);
    List<String> imageFileName = new ArrayList<String>();
    images.forEach(image -> imageFileName.add(image.getFileName()));
    List<String> imagesSrc = new ArrayList<String>();
    imageFileName.forEach(fileName ->imagesSrc.add(findImageSrc(owner,house,fileName)));
    houseInfo.setImagesSrc(imagesSrc.toArray(new String[0]));
    return houseInfo;
  }

  private String findImageSrc(User owner, House house, String fileName) {
    String userName = owner.getUsername();
    String houseName = house.getName();
    return "resources/images/" + userName + "/" + houseName + "/" + fileName;
  }
}






