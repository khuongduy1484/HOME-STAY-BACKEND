package com.demo.controller;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.demo.message.response.JwtResponse;
import com.demo.message.response.ResponseMessage;
import com.demo.message.request.LoginForm;
import com.demo.message.request.SignUpForm;
import com.demo.model.ConfirmationToken;
import com.demo.model.Role;
import com.demo.model.RoleName;
import com.demo.model.User;
import com.demo.repository.ConfirmationTokenRepository;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import com.demo.security.jwt.JwtProvider;
import com.demo.security.services.EmailSenderService;
import com.demo.security.services.MultipartFileService;
import com.demo.security.services.UserPrinciple;
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
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

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
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
        HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
        HttpStatus.BAD_REQUEST);
    }

    // Creating user's account

    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
      encoder.encode(signUpRequest.getPassword()));
    user.setEnabled(false);
    String avatarFileName = signUpRequest.getAvatar().getOriginalFilename();
    user.setAvatarFileName(avatarFileName);
    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
      .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
    roles.add(userRole);
    user.setRoles(roles);
    String saveLocation = UPLOAD_LOCATION+user.getUsername()+"\\avatar\\";
    new File(saveLocation).mkdirs();
    multipartFileService.saveMultipartFile(saveLocation, signUpRequest.getAvatar(), avatarFileName);

    userRepository.save(user);
    emailSenderService.sendEmail(user);

    return new ResponseEntity<>(new ResponseMessage("Please login your email to confirm"), HttpStatus.OK);
  }

  @GetMapping(value = "confirm-account")
  public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
      user.setEnabled(true);
      userRepository.save(user);
      return new ResponseEntity<>(new ResponseMessage("User registered successfully!"),
        HttpStatus.OK);

    } else {
      return new ResponseEntity<>(new ResponseMessage("Fail -> DANG KI THAT BAI"),
        HttpStatus.BAD_REQUEST);
    }

  }
}