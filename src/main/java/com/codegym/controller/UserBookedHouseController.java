package com.codegym.controller;

import com.codegym.message.request.BookHouseForm;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.House;
import com.codegym.model.User;
import com.codegym.model.UserBookedHouse;
import com.codegym.security.jwt.JwtAuthTokenFilter;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.service.HouseService;
import com.codegym.service.UserBookedHouseService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/book")
public class UserBookedHouseController {
  @Autowired
  HouseService houseService;
  @Autowired
  UserBookedHouseService userBookedHouseService;
  @Autowired
  UserService userService;
  @Autowired
  JwtAuthTokenFilter authTokenFilter;
  @Autowired
  JwtProvider jwtProvider;

  @PostMapping("/{id}")
  public ResponseEntity<?> bookHouse(@Valid @RequestBody BookHouseForm userBookedHouse,
                                     @PathVariable("id") Long id) {
    Date nowDate = new Date();
    Boolean after = userBookedHouse.getCheckOut().after(userBookedHouse.getCheckIn());
    Boolean before = nowDate.before(userBookedHouse.getCheckIn());

    User user = userService.getUserByAuth();
    House house = houseService.findById(id).orElseThrow(()
      -> new UsernameNotFoundException("House is not found : "));
    if (house == null || house.getIsRented()) {
      return new ResponseEntity<>(new ResponseMessage("House is deleted or rented"),
        HttpStatus.BAD_REQUEST);
    }
    if (after && before) {
      UserBookedHouse bookedHouse = new UserBookedHouse(user, house, nowDate,
        userBookedHouse.getCheckIn(), userBookedHouse.getCheckOut());
      house.setIsRented(true);
      houseService.save(house);
      userBookedHouseService.save(bookedHouse);
      return new ResponseEntity<>(new ResponseMessage("Rented  successfully"), HttpStatus.OK);
    }
    return new ResponseEntity<>(new ResponseMessage("House is deleted or rented"), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<UserBookedHouse>>listBookHouseByUser(){
    User user = userService.getUserByAuth();
    List<UserBookedHouse>userBookedHouses = userBookedHouseService.findByUserBook(user);
    if (userBookedHouses.isEmpty()){
      return  new ResponseEntity<List<UserBookedHouse>>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<UserBookedHouse>>(userBookedHouses,HttpStatus.OK);
  }

  @GetMapping("owner/{name}")
  public ResponseEntity<?>searchBookHouse(@PathVariable("name") String name){
    UserBookedHouse userBookedHouse = userBookedHouseService.findByHouseName(name);
    User user = userBookedHouse.getUser();
    if (userBookedHouse != null){
      return new ResponseEntity<UserBookedHouse>(userBookedHouse,HttpStatus.OK);
    }
    return  new ResponseEntity<UserBookedHouse>(HttpStatus.NO_CONTENT);

  }

@DeleteMapping("{id}")
  public ResponseEntity<?>deleteBooking(@PathVariable("id")Long id){
    User user = userService.getUserByAuth();
    UserBookedHouse userBookedHouse = userBookedHouseService.findById(id);
    Boolean a = userBookedHouseService.validateTime(userBookedHouse);
  System.out.println(a);
   if (userBookedHouseService.validateTime(userBookedHouse)){
     House house = userBookedHouse.getHouse();
     house.setIsRented(false);
     houseService.save(house);
     userBookedHouseService.delete(userBookedHouse);
     return new ResponseEntity<>(new ResponseMessage("Delete book house successfully"),HttpStatus.OK);
   }
   return new ResponseEntity<>(new ResponseMessage("cannot delete book"),HttpStatus.BAD_REQUEST);
}
}
