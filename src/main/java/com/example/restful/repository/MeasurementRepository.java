package com.example.restful.repository;

import com.example.restful.entity.DbMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeasurementRepository extends JpaRepository<DbMeasurement, Integer> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
