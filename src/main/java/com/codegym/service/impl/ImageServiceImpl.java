package com.codegym.service.impl;

import com.codegym.model.Image;
import com.codegym.repository.ImageRespository;
import com.codegym.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
  @Autowired
  ImageRespository imageRespository;

  @Override
  public Image save(Image image) {
    return imageRespository.save(image);
  }
}
