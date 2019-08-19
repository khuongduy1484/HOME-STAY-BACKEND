package com.codegym.repository;

import com.codegym.model.House;
import com.codegym.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRespository extends JpaRepository<Image, Long> {
  List<Image> findAllByHouse(House house);
  Optional<Image> findByImageUrl(String imageUrl);
}
