package com.example.restful.repository;

import com.example.restful.entity.DbInputProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InputProductRepository extends JpaRepository<DbInputProduct, Integer> {
    List<DbInputProduct> findAllByDbInput_Id(Integer dbInput_id);
}
