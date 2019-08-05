package com.codegym.repository;

import com.codegym.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
  Boolean existsByName(String name);

  House findByName(String name);
}
