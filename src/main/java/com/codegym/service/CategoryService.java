package com.codegym.service;

import com.codegym.model.Category;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
  Category save(Category category);

  Category findById(Long id);

  Category findByName(String name) throws EntityNotFoundException;

  List<Category> findAll();
}