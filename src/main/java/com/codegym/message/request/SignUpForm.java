package com.codegym.message.request;

import com.codegym.model.Gender;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;

public class SignUpForm {
  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @NotBlank
  @Size(max = 60)
  @Email
  private String email;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Past
  private Date birthday;

  private Gender gender;

  private String address;

  @Pattern(regexp = "0([0-9]{9,10})")
  private String phoneNumber;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  private MultipartFile avatar;

  public MultipartFile getAvatar() {
    return avatar;
  }

  public void setAvatar(MultipartFile avatar) {
    this.avatar = avatar;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

}

