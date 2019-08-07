package com.codegym.service;

import com.codegym.model.House;
import com.codegym.model.Image;

import java.util.List;

public interface ImageService {
  Image save(Image image);
  List<Image> findAllByHouse(House house);
}
