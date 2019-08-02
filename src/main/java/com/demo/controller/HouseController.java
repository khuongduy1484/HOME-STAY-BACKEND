package com.demo.controller;

import com.demo.message.request.CreateHouseForm;
import com.demo.message.response.ResponseMessage;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.repository.ImageRespository;
import com.demo.security.jwt.JwtAuthTokenFilter;
import com.demo.security.jwt.JwtProvider;
import com.demo.security.services.MultipartFileService;
import com.demo.security.services.UserDetailsServiceImpl;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
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
  HouseRepository houseRepository;
  @Autowired
  ImageRespository imageRespository;

  @Value("${upload.location}")
  private String UPLOAD_LOCATION;
  @PostMapping(value = "create-house", consumes = "multipart/form-data")
//  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> createHouse(@ModelAttribute CreateHouseForm createHouseForm, HttpServletRequest request){
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

    if (user != null){
      House house = new House(createHouseForm.getName(),createHouseForm.getAddress(),createHouseForm.getBedRooms(),createHouseForm.getBathRooms(),createHouseForm.getDescribe(),createHouseForm.getPricePerNight());
      MultipartFile[] gallery = createHouseForm.getImages();

//      Arrays.stream(createHouseForm.getImages()).map(file ->UPLOAD_LOCATION).collect(Collectors.toList());
//     while (iterator.hasNext()){
//       String imageHouse =iterator.next().getOriginalFilename();
//     Set<MultipartFile> houseImage = new HashSet<>();
////     houseImage.add()
      for (MultipartFile file : gallery) {

      }

      houseRepository.save(house);
      return new ResponseEntity<>(new ResponseMessage("tao nha thanh cong"),HttpStatus.OK);
    }
//    }else {
//      return new ResponseEntity<>(new ResponseMessage("tao nha that bat"),HttpStatus.BAD_REQUEST);
//    }
    return new ResponseEntity<>(new ResponseMessage("tao nha thanh cong"),HttpStatus.OK);
  }

}

