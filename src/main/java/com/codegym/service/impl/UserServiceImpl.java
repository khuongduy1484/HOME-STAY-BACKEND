package com.codegym.service.impl;

import com.codegym.model.House;
import com.codegym.model.User;
import com.codegym.repository.UserRepository;
import com.codegym.security.services.UserPrinciple;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User findByPhoneNumber(String phoneNumber) {
    return userRepository.findByPhoneNumber(phoneNumber);
  }

  @Override
  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public Boolean existsByPhoneNumber(String phoneNumber) {
    return userRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public User findByEmailIgnoreCase(String email) {
    return userRepository.findByEmailIgnoreCase(email);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> findUsersByHouses(House houses){
    return userRepository.findUsersByHouses(houses);
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).get();
  }

  @Override
  public User getUserByAuth() {
      Object userPrinciple = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long user_id = ((UserPrinciple) userPrinciple).getId();
    return findById(user_id);  }

}
