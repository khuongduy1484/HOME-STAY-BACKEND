package com.demo.service.impl;

import com.demo.model.Role;
import com.demo.model.RoleName;
import com.demo.repository.RoleRepository;
import com.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
  @Autowired
  RoleRepository roleRepository;
  @Override
  public Optional<Role> findByName(RoleName roleName) {
    return roleRepository.findByName(roleName);
  }
}
