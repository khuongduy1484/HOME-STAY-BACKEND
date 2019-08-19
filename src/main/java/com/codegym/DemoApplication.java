package com.codegym;



import com.codegym.formatter.CategoryFormatter;
import com.codegym.formatter.ImageFormatter;
import com.codegym.service.CategoryService;
import com.codegym.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
  @Bean
  public CategoryService categoryService(){return new CategoryServiceImpl();
  }
  @Configuration
  static class WebMvcConfig implements WebMvcConfigurer {
//    @Autowired
//    CategoryService categoryService;
    @Override
    public void addFormatters(FormatterRegistry registry) {
      // registry.addFormatter(new CategoryFormatter(categoryService));
      registry.addFormatter(new ImageFormatter());
    }
  }
}
