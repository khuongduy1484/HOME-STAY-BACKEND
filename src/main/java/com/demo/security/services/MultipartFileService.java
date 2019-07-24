package com.demo.security.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("multipartFileService")
public class MultipartFileService {
  public void saveMultipartFile(String saveLocation, MultipartFile multipartFile, String avatarFileName) {
    try {
      byte[] bytes = multipartFile.getBytes();
      Path path = Paths.get(saveLocation + avatarFileName);
      Files.write(path, bytes);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
