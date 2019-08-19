package com.codegym.service.impl;

import com.codegym.model.House;
import com.codegym.model.Image;
import com.codegym.repository.ImageRespository;
import com.codegym.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
  @Autowired
  ImageRespository imageRespository;

  @Override
  public Image save(Image image) {
    return imageRespository.save(image);
  }

  @Override
  public List<Image> findAllByHouse(House house) {
    return imageRespository.findAllByHouse(house);
  }

  @Override
  public Image findByImageUrl(String imageUrl) throws EntityNotFoundException {
    return imageRespository.findByImageUrl(imageUrl).orElseThrow(EntityNotFoundException::new);
  }
}
