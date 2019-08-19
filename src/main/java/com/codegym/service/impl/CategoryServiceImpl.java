package com.codegym.service.impl;

import com.codegym.model.Category;
import com.codegym.repository.CategoryRepository;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


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
  public Category findByName(String name) throws EntityNotFoundException {
    return categoryRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }


}
