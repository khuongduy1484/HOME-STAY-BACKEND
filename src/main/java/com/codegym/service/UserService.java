package com.codegym.service;

import com.codegym.model.House;
import com.codegym.model.User;
import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> findByUsername(String username);

  User findByPhoneNumber(String phoneNumber);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Boolean existsByPhoneNumber(String phoneNumber);

  User findByEmailIgnoreCase(String email);

  User save(User user);
  List<User> findUsersByHouses(House houses);

  User findById(Long id);

  User getUserByAuth();
}
