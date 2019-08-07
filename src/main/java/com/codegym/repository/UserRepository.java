package com.codegym.repository;

import com.codegym.model.House;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  User findByPhoneNumber(String phoneNumber);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Boolean existsByPhoneNumber(String phoneNumber);

  User findByEmailIgnoreCase(String email);

  List<User> findUsersByHouses(House houses);
}