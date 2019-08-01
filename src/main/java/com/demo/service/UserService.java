package com.demo.service;

import com.demo.model.User;

import java.util.Optional;

public interface UserService {
  Optional<User> findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);
  Boolean existsByPhoneNumber(String phoneNumber);
  User findByEmailIgnoreCase(String email);
  User save(User user);
}
