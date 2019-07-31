package com.demo.service;

import com.demo.model.Role;
import com.demo.model.RoleName;

import java.util.Optional;

public interface RoleService {
  Optional<Role> findByName(RoleName roleName);
}
