package com.demo.model;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  @NaturalId
  @Column(length = 60)
  private CategoryName categoryName;
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private Set<House> listHouse = new HashSet<>();

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

  public CategoryName getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(CategoryName categoryName) {
    this.categoryName = categoryName;
  }
}
