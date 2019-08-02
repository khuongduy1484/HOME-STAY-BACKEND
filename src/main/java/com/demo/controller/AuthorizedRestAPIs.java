package com.demo.controller;

import com.demo.message.request.CreateHouseForm;
import com.demo.message.request.UpdateInfoForm;
import com.demo.message.request.UpdatePasswordForm;
import com.demo.message.response.JwtResponse;
import com.demo.message.response.ResponseMessage;
import com.demo.model.House;
import com.demo.model.Image;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthorizedRestAPIs {
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

  @GetMapping("/test/user")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String userAccess() {
    return ">>> User Contents!";
  }

  @GetMapping("/test/pm")
  @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
  public String projectManagementAccess() {
    return ">>> Project Management Board";
  }

  @GetMapping("/test/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return ">>> Admin Contents";
  }

  @PutMapping(value = "/update-info", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> updateInfo(HttpServletRequest request, @Valid @ModelAttribute UpdateInfoForm updateInfoForm) {
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
    if (updateInfoForm.getName() != null) {
      user.setName(updateInfoForm.getName());
    }
    if (updateInfoForm.getBirthday()!=null){
      user.setBirthday(updateInfoForm.getBirthday());
    }
    if (updateInfoForm.getGender()!=null){
      user.setGender(updateInfoForm.getGender());
    }
    if (updateInfoForm.getAddress()!=null){
      user.setAddress(updateInfoForm.getAddress());
    }
    if (updateInfoForm.getPhoneNumber()!=null){
      if (userService.existsByPhoneNumber(updateInfoForm.getPhoneNumber())){
        return new ResponseEntity<>(new ResponseMessage("Fail -> Phone number is already in use"),
          HttpStatus.BAD_REQUEST);
      }
      user.setPhoneNumber(updateInfoForm.getPhoneNumber());
    }
    if (updateInfoForm.getAvatar()!=null){
      String avatarFileName = updateInfoForm.getAvatar().getOriginalFilename();
      user.setAvatarFileName(avatarFileName);
      String saveLocation = UPLOAD_LOCATION+user.getUsername()+"/avatar/";
      multipartFileService.saveMultipartFile(saveLocation, updateInfoForm.getAvatar(), avatarFileName);
    }
    User save = userService.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
    String avatarLink =user.getAvatarFileName()!=null? "resources/images/"+userDetails.getUsername()+"/avatar/"+user.getAvatarFileName():"";
    JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(),avatarLink);
    return ResponseEntity.ok(jwtResponse);
  }

  @PutMapping(value = "update-password")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordForm updatePasswordForm, HttpServletRequest request) {
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
    boolean matches = encoder.matches(updatePasswordForm.getCurrentPassword(), user.getPassword());
    String newPassword = encoder.encode(updatePasswordForm.getNewPassword());
    if (newPassword != null) {
      if (matches) {
        user.setPassword(newPassword);
        userService.save(user);
      }
      else {
        return new ResponseEntity<>(new ResponseMessage("Incorrect password"),
          HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity<>(new ResponseMessage("Password successfully reset"), HttpStatus.OK);
  }


}