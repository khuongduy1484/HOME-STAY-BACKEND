package com.codegym.repository;

import com.codegym.model.House;
import com.codegym.model.QHouse;
import com.codegym.model.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>, QuerydslPredicateExecutor<House>, QuerydslBinderCustomizer<QHouse> {
  Boolean existsByName(String name);

  House findByName(String name);
  List<House> findAllByBathRooms(Integer bathRooms);
  List<House> findAllByBedRooms(Integer bedRooms);
  List<House> findAllByOwner(User user);
  List<House> findAllByPricePerNightBetween(Integer minPrice, Integer maxPrice);
  @Override
  default void customize(QuerydslBindings bindings, QHouse root) {
    bindings.bind(String.class).first(
      (SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    bindings
      .bind(Integer.class)
      .first((SingleValueBinding<NumberPath<Integer>, Integer>) NumberExpression::goe);
    bindings
      .bind(Integer.class)
      .first((SingleValueBinding<NumberPath<Integer>, Integer>) NumberExpression::loe);
    bindings
      .bind(Integer.class)
      .first((SingleValueBinding<NumberPath<Integer>, Integer>) NumberExpression::eq);
  }
}
