package com.example.restful.repository;

import com.example.restful.entity.DbWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<DbWarehouse, Integer> {
    List<DbWarehouse> findAllByActive(boolean active);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
