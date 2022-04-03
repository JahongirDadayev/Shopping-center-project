package com.example.restful.repository;

import com.example.restful.entity.DbOutputProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutputProductRepository extends JpaRepository<DbOutputProduct, Integer> {
    List<DbOutputProduct> findAllByDbOutput_Id(Integer dbOutput_id);
}
