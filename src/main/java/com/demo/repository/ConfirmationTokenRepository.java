package com.demo.repository;

import com.demo.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
  ConfirmationToken findByConfirmationToken(String confirmationToken);
}
