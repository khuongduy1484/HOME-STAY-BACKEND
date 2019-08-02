package com.demo.service.impl;

import com.demo.model.Category;
import com.demo.repository.CategoryRepository;
import com.demo.service.CategoryService;
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
}
