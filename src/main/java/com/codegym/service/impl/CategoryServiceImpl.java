package com.codegym.service.impl;

import com.codegym.model.Category;
import com.codegym.repository.CategoryRepository;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {

@Autowired
  CategoryRepository categoryRepository;
  @Override
  public Category save(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Category findById(Long id) {
    return categoryRepository.findById(id).get();
  }


  @Override
  public Category findByCategoryName(String name) {
    return categoryRepository.findByCategoryName(name);
  }


}
