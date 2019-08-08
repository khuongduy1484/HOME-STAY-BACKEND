package com.codegym.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  @OneToMany(targetEntity = House.class)
  private Set<House> listHouse;

  public Set<House> getListHouse() {
    return listHouse;
  }

  public void setListHouse(Set<House> listHouse) {
    this.listHouse = listHouse;
  }

  public Category() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
