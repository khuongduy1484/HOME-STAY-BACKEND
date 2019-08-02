package com.demo.message.request;

import com.demo.model.Gender;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UpdateInfoForm {
  @Size(min = 3, max = 50)
  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Past
  private Date birthday;

  private Gender gender;

  private String address;

  @Pattern(regexp = "0([0-9]{9,10})")
  private String phoneNumber;

  private MultipartFile avatar;

  public UpdateInfoForm() {
  }

  public UpdateInfoForm(@Size(min = 3, max = 50) String name, @Past Date birthday, Gender gender, String address, String phoneNumber, MultipartFile avatar) {
    this.name = name;
    this.birthday = birthday;
    this.gender = gender;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.avatar = avatar;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public MultipartFile getAvatar() {
    return avatar;
  }

  public void setAvatar(MultipartFile avatar) {
    this.avatar = avatar;
  }
}
