package com.demo.repository;

import com.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRespository extends JpaRepository<Image,Long> {
}
