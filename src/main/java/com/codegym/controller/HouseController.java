package com.codegym.controller;

import com.codegym.message.request.CreateHouseForm;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.*;
import com.codegym.repository.CategoryRepository;
import com.codegym.repository.ImageRespository;
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
import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
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

  @Value("${upload.location.house.image}")
  private String UPLOAD_LOCATION_HOUSE_IMAGE;

  @PostMapping(value = "create-house", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> createHouse(@ModelAttribute CreateHouseForm createHouseForm, HttpServletRequest request) {
    String jwts = authenticationJwtTokenFilter.getJwt(request);
    String userName = jwtProvider.getUserNameFromJwtToken(jwts);
    User user;
    try {
      user = userService.findByUsername(userName).orElseThrow(
        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + userName));
    }
    catch (UsernameNotFoundException exception) {
      return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

      House house = new House(createHouseForm.getName(), createHouseForm.getAddress(), createHouseForm.getBedRooms(),
        createHouseForm.getBathRooms(), createHouseForm.getDescription(), createHouseForm.getPricePerNight());
      house.setOwner(user);
      Category categoryName = categoryService.findByCategoryName(createHouseForm.getCategory());
      house.setCategory(categoryName);
      houseService.save(house);
      House houseName = houseService.findByName(createHouseForm.getName());
      MultipartFile[] gallery = createHouseForm.getImages();
      Set<Image> houseImages = new HashSet<>();
      multipartFileService.saveGallery(userName, houseName, gallery, houseImages ,UPLOAD_LOCATION_HOUSE_IMAGE);

    return new ResponseEntity<>(new ResponseMessage("Publish House successfully"), HttpStatus.OK);
  }

}






