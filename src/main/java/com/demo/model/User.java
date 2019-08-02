package com.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "users", uniqueConstraints = {
  @UniqueConstraint(columnNames = {
    "username"
  }),
  @UniqueConstraint(columnNames = {
    "email"
  })
})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  private boolean enabled;

  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @NaturalId
  @NotBlank
  @Size(max = 50)
  @Email
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Past
  private Date birthday;

  @Enumerated(EnumType.ORDINAL)
  private Gender gender;

  private String address;

  @Pattern(regexp = "0([0-9]{9,10})")
  @Column(unique = true)
  private String phoneNumber;

  @NotBlank
  @Size(min = 6, max = 100)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  private String avatarFileName;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public User() {
  }

  public User(@NotBlank @Size(min = 3, max = 50) String name, @NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(max = 50) @Email String email, Date birthday, Gender gender, String address, String phoneNumber, @NotBlank @Size(min = 6, max = 100) String password) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.birthday = birthday;
    this.gender = gender;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getAvatarFileName() {
    return avatarFileName;
  }

  public void setAvatarFileName(String avatarFileName) {
    this.avatarFileName = avatarFileName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthday() { return birthday; }

  public void setBirthday(Date birthday) { this.birthday = birthday; }

  public Gender getGender() { return gender; }

  public void setGender(Gender gender) { this.gender = gender; }

  public String getAddress() { return address; }

  public void setAddress(String address) { this.address = address; }

  public String getPhoneNumber() { return phoneNumber; }

  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getPassword() { return password; }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}