package com.codegym.controller;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.codegym.message.request.ResetPasswordForm;
import com.codegym.message.response.JwtResponse;
import com.codegym.message.response.ResponseMessage;
import com.codegym.message.request.LoginForm;
import com.codegym.message.request.SignUpForm;
import com.codegym.model.*;
import com.codegym.repository.ConfirmationTokenRepository;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.services.EmailSenderService;
import com.codegym.security.services.MultipartFileService;
import com.codegym.security.services.UserPrinciple;
import com.codegym.service.RoleService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
  @Autowired
  private ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
  private EmailSenderService emailSenderService;

  @Autowired
  private MultipartFileService multipartFileService;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService userService;

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtProvider jwtProvider;
  @Value("${upload.location}")
  private String UPLOAD_LOCATION;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtProvider.generateJwtToken(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    UserPrinciple userPrinciple = (UserPrinciple) userDetails;
    String avatarLink =userPrinciple.getAvatarFileName()!=null? "resources/images/"+userDetails.getUsername()+"/avatar/"+userPrinciple.getAvatarFileName():"";
    JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(),avatarLink);
    return ResponseEntity.ok(jwtResponse);
  }

  @PostMapping(value = "/signup", consumes = "multipart/form-data")
  public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignUpForm signUpRequest) {
    System.out.println(signUpRequest);
    if (userService.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
        HttpStatus.BAD_REQUEST);
    }

    if (userService.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
        HttpStatus.BAD_REQUEST);
    }

    if (userService.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Phone number is already in use"),
        HttpStatus.BAD_REQUEST);
    }

    // Creating user's account

    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
      signUpRequest.getBirthday(), signUpRequest.getGender(), signUpRequest.getAddress(),
      signUpRequest.getPhoneNumber(), encoder.encode(signUpRequest.getPassword()));
    user.setEnabled(false);
    String avatarFileName = signUpRequest.getAvatar().getOriginalFilename();
    user.setAvatarFileName(avatarFileName);

    Set<Role> roles = new HashSet<>();
    Role userRole = roleService.findByName(RoleName.ROLE_USER)
      .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
    roles.add(userRole);
    user.setRoles(roles);

    String saveLocation = UPLOAD_LOCATION+user.getUsername()+"/avatar/";
    new File(saveLocation).mkdirs();
    multipartFileService.saveMultipartFile(saveLocation, signUpRequest.getAvatar(), avatarFileName);

    userService.save(user);
    emailSenderService.sendEmailCreateUser(user);

    return new ResponseEntity<>(new ResponseMessage("Please login your email to confirm"), HttpStatus.OK);
  }

  @GetMapping(value = "confirm-account")
  public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      User user = userService.findByEmailIgnoreCase(token.getUser().getEmail());
      user.setEnabled(true);
      userService.save(user);
      return new ResponseEntity<>(new ResponseMessage("User registered successfully!"),
        HttpStatus.OK);

    } else {
      return new ResponseEntity<>(new ResponseMessage("Fail -> User's register occurred errors!"),
        HttpStatus.BAD_REQUEST);
    }

  }


  @PostMapping(value = "/forgot-password")
  public ResponseEntity<?> forgotUserPassword(@RequestBody String gmail) {
    User existingUser = userService.findByEmailIgnoreCase(gmail);
    if (existingUser != null) {
      existingUser.setEnabled(false);
      userService.save(existingUser);
      emailSenderService.sendEmailForgotPassword(existingUser);
      return new ResponseEntity<>(
        new ResponseMessage("Request to reset password received. Check your inbox for the reset link"),
        HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(new ResponseMessage("This email address does not exist!"),
        HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/confirm-reset")
  public ResponseEntity<?> validateResetToken(@RequestParam("token") String confirmationToken) {
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    if (token != null) {
      User user = userService.findByEmailIgnoreCase(token.getUser().getEmail());
      user.setEnabled(true);
      userService.save(user);
      return new ResponseEntity<>(new ResponseMessage("resetPassword"),
        HttpStatus.OK);

    }
    else {
      return new ResponseEntity<>(new ResponseMessage("The link is invalid or broken!"),
        HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/reset-password")
  public ResponseEntity<?>resetUserPassword( @RequestBody ResetPasswordForm resetPasswordForm){
    if (resetPasswordForm.getGmail() !=null){
      User tokenUser = userService.findByEmailIgnoreCase(resetPasswordForm.getGmail());
      tokenUser.setPassword(encoder.encode(resetPasswordForm.getPassword()));
      userService.save(tokenUser);
      return new ResponseEntity<>(new ResponseMessage("Password successfully reset. You can now log in with the new credentials"),HttpStatus.OK);
    }else {
      return new ResponseEntity<>(new ResponseMessage("The link is invalid or broken!"),
        HttpStatus.BAD_REQUEST);
    }
  }
}
