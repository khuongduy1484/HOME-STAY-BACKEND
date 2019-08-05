package com.codegym.service;

import com.codegym.model.Category;

import java.util.Optional;

public interface CategoryService {
  Category save(Category category);

  Category findById(Long id);

  Category findByCategoryName(String name);


}