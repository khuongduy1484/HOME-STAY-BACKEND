package com.codegym.repository;

import com.codegym.model.House;
import com.codegym.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRespository extends JpaRepository<Image, Long> {
  List<Image> findAllByHouse(House house);
}
