package com.example.restful.repository;

import com.example.restful.entity.DbProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<DbProduct, Integer> {
    List<DbProduct> findAllByDbCategory_Id(Integer dbCategory_id);

    boolean existsByName(String name);

    Optional<DbProduct> findTopByOrderByIdDesc();

    boolean existsByNameAndIdNot(String name, Integer id);
}
