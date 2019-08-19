package com.codegym.formatter;

import com.codegym.model.Category;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.util.Locale;

public class CategoryFormatter implements Formatter<Category> {
  @Autowired
  private CategoryService categoryService;

  public CategoryFormatter(CategoryService categoryService) {
    this.categoryService = categoryService;
  }
  @Override
  public Category parse(String name, Locale locale) throws ParseException {
    if (!name.equals("")) {
      try {
//        Long id_ = Long.parseLong(name);
        Category category = categoryService.findByName(name);
        String foo = "foo";
        return category;
      } catch (EntityNotFoundException exception){
        return new Category(name);
      } catch (Exception e){
        System.out.println(e);
      }
    }
    return null;
  }

  @Override
  public String print(Category category, Locale locale) {
    return category.getName();
  }
}
