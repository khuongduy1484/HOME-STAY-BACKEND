package com.codegym.controller;

import com.codegym.message.request.UpdateInfoForm;
import com.codegym.message.request.UpdatePasswordForm;
import com.codegym.message.response.JwtResponse;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.User;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.MultipartFileService;
import com.codegym.security.services.UserDetailsServiceImpl;
import com.codegym.security.services.UserPrinciple;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  private MultipartFileService multipartFileService;

  @Autowired
  JwtAuthTokenFilter authenticationJwtTokenFilter;
  @Autowired
  JwtProvider jwtProvider;
  @Autowired
  PasswordEncoder encoder;


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
    try {
      User user = getUser(request);
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
        if (isExistedByPhoneNumber(user,updateInfoForm.getPhoneNumber())){
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
      UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
      String avatarLink =user.getAvatarFileName()!=null? "resources/images/"+userDetails.getUsername()+"/avatar/"+user.getAvatarFileName():"";
      JwtResponse jwtResponse = new JwtResponse(authenticationJwtTokenFilter.getJwt(request), userDetails.getUsername(), userDetails.getAuthorities(),avatarLink);
      return ResponseEntity.ok(jwtResponse);
    } catch (UsernameNotFoundException exception) {
      return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
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
