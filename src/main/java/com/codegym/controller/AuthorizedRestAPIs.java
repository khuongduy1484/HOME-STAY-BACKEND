package com.codegym.controller;

import com.codegym.message.request.UpdateInfoForm;
import com.codegym.message.request.UpdatePasswordForm;
import com.codegym.message.response.JwtResponse;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.User;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.security.services.UserPrinciple;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthorizedRestAPIs {
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
    User user = userService.getUserByAuth();
    user.setName(updateInfoForm.getName());
    user.setAddress(updateInfoForm.getAddress());
    user.setBirthday(updateInfoForm.getBirthday());
    user.setGender(updateInfoForm.getGender());
    if (isExistedByPhoneNumber(user, updateInfoForm.getPhoneNumber())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Phone number is already in use"),
        HttpStatus.BAD_REQUEST);
    }
    user.setPhoneNumber(updateInfoForm.getPhoneNumber());
    user.setAvatarUrl(updateInfoForm.getAvatarUrl());
    User save = userService.save(user);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
    JwtResponse jwtResponse = new JwtResponse(authenticationJwtTokenFilter.getJwt(request), userDetails.getUsername(),
      userDetails.getAuthorities(), user.getAvatarUrl());
    return ResponseEntity.ok(jwtResponse);

  }

  @GetMapping("update-info")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<UpdateInfoForm> getUpdateInfoForm(){
    User user = userService.getUserByAuth();
    UpdateInfoForm updateInfoForm = new UpdateInfoForm(user.getName(),user.getBirthday(),user.getGender(),user.getAddress(),user.getPhoneNumber(),user.getAvatarUrl());
    return new ResponseEntity<UpdateInfoForm>(updateInfoForm,HttpStatus.OK);
  }

  @PutMapping(value = "update-password")
  @PreAuthorize("hasRole('USER') or hasRole('PM') or hasRole('ADMIN')")
  public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordForm updatePasswordForm, HttpServletRequest request) {
    try {
      User user = getUser(request);
      String newPassword = encoder.encode(updatePasswordForm.getNewPassword());
      if (newPassword != null) {
        boolean matches = encoder.matches(updatePasswordForm.getCurrentPassword(), user.getPassword());
        if (matches) {
          user.setPassword(newPassword);
          userService.save(user);
          return new ResponseEntity<>(new ResponseMessage("Password successfully reset"), HttpStatus.OK);
        }
        else {
          return new ResponseEntity<>(new ResponseMessage("Incorrect password"),
            HttpStatus.BAD_REQUEST);
        }
      }
      return new ResponseEntity<>(new ResponseMessage("New Password must not be blank"), HttpStatus.BAD_REQUEST);
    }
    catch (UsernameNotFoundException exception) {
      return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
  }


  private boolean isExistedByPhoneNumber(User user, String phoneNumber) {
    UserPrinciple userPrinciple = UserPrinciple.build(user);
    UserPrinciple userExistsByPhoneNumber = UserPrinciple.build(userService.findByPhoneNumber(phoneNumber));
    return userService.existsByPhoneNumber(phoneNumber) && (!userPrinciple.equals(userExistsByPhoneNumber));
  }

  private User getUser(HttpServletRequest request) throws UsernameNotFoundException {
    String jwt = authenticationJwtTokenFilter.getJwt(request);
    String userName = jwtProvider.getUserNameFromJwtToken(jwt);
    User user = userService.findByUsername(userName).orElseThrow(
      () -> new UsernameNotFoundException("User Not Found with -> username or email : " + userName));
    return user;
  }
}

