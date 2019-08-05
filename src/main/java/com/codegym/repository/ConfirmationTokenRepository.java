package com.codegym.repository;

import com.codegym.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
  ConfirmationToken findByConfirmationToken(String confirmationToken);
}
