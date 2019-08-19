package com.codegym.formatter;

import com.codegym.model.Image;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ImageFormatter implements Formatter<Image> {
  @Override
  public Image parse(String imageUrl, Locale locale) throws ParseException {
    return new Image(imageUrl);
  }

  @Override
  public String print(Image image, Locale locale) {
    return image.getImageUrl();
  }
}
