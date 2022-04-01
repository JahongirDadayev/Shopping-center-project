package com.example.restful.repository;

import com.example.restful.entity.DbCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<DbCategory, Integer> {
    List<DbCategory> findAllByParentCategory_Id(Integer parentCategory_id);

    boolean existsByNameAndParentCategory_Id(String name, Integer parentCategory_id);

    boolean existsByNameAndParentCategory_IdAndIdNot(String name, Integer parentCategory_id, Integer id);
}
