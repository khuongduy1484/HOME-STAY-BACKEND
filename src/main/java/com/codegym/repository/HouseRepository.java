package com.codegym.repository;

import com.codegym.model.House;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
  Boolean existsByName(String name);

  House findByName(String name);

  List<House> findAllByOwner(User user);

}
