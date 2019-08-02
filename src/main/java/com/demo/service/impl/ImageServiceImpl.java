package com.demo.service.impl;

import com.demo.model.Image;
import com.demo.repository.ImageRespository;
import com.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageServiceImpl implements ImageService {
  @Autowired
  ImageRespository imageRespository;
  @Override
  public Image save(Image image) {
    return imageRespository.save(image);
  }
}
