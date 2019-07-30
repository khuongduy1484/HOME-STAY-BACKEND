package com.demo.message.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

public class UpdateInfoForm {
  @Size(min = 3, max = 50)
  private String name;
  private MultipartFile avatar;

  public UpdateInfoForm() {
  }

  public UpdateInfoForm(@Size(min = 3, max = 50) String name, MultipartFile avatar) {
    this.name = name;
    this.avatar = avatar;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public MultipartFile getAvatar() {
    return avatar;
  }

  public void setAvatar(MultipartFile avatar) {
    this.avatar = avatar;
  }
}
