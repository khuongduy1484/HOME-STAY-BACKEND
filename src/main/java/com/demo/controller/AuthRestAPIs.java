package com.demo.controller;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;

import com.demo.message.reponse.JwtResponse;
import com.demo.message.reponse.ResponseMessage;
import com.demo.message.request.LoginForm;
import com.demo.model.ConfirmationToken;
import com.demo.model.RoleName;
import com.demo.model.User;
import com.demo.repository.ConfirmationTokenRepository;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import com.demo.security.jwt.JwtProvider;
import com.demo.security.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

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
    private static String UPLOAD_LOCATION = "/home/duy/MODULE_2/security/src/main/resources/images/";

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestParam("username") String username, @RequestParam("name")String name, @RequestParam("password") String password , @Valid @RequestParam("email") String email, @RequestParam("avata") MultipartFile multipartFile) {
        if (userRepository.existsByUsername(username)) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        String fileName = multipartFile.getOriginalFilename();
        try{
            FileCopyUtils.copy(fileName.getBytes(),new File(UPLOAD_LOCATION +fileName));
        }catch (IOException ex){
            ex.printStackTrace();
        }

        // Creating user's account
        User user = new User(name, username, email, encoder.encode(password), fileName);
        user.setEnabled(false);

//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
        user.setRoles(roleRepository.findByName(RoleName.ROLE_USER).get());
        userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("khuongduy1484@gmail");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/api/auth/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        return new ResponseEntity<>(new ResponseMessage("Please login your email to confirm"), HttpStatus.OK);
    }
    @GetMapping (value = "confirm-account")
    public  ResponseEntity<?> confirmUserAccount( @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            return new ResponseEntity<>(new ResponseMessage("User registered successfully!"),
                    HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>(new ResponseMessage("Fail -> DANG KI THAT BAI"),
                    HttpStatus.BAD_REQUEST);
        }
    }


}