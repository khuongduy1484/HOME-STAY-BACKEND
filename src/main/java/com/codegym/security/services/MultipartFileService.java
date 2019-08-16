package com.codegym.security.services;

import com.codegym.model.House;
import com.codegym.model.Image;
import com.codegym.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Service("multipartFileService")
public class MultipartFileService {
  @Autowired
  ImageServiceImpl imageService;
  public void saveMultipartFile(String saveLocation, MultipartFile multipartFile, String fileName) {
    try {
      byte[] bytes = multipartFile.getBytes();
      Path path = Paths.get(saveLocation + fileName);
      Files.write(path, bytes);
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }
  }
  public void saveGallery(String userName, House house, MultipartFile[] gallery, Set<Image> houseImages, String saveDirectory) {
    for (MultipartFile photo : gallery) {
      String imageFileName = photo.getOriginalFilename();
      Image image = new Image();
      image.setFileName(imageFileName);
      houseImages.add(image);
      image.setHouse(house);
      imageService.save(image);
      String houseImageLocation = saveDirectory +"/" + userName + "/" + house.getName() + "/" ;
      new File(houseImageLocation).mkdir();
      saveMultipartFile(houseImageLocation, photo, imageFileName);
    }
  }
}
