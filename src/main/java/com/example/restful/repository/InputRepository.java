package com.example.restful.repository;

import com.example.restful.entity.DbInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InputRepository extends JpaRepository<DbInput, Integer> {
    Page<DbInput> findAllByDbWarehouse_Id(Integer dbWarehouse_id, Pageable pageable);

    boolean existsByFactureNumber(String factureNumber);

    Optional<DbInput> findTopByOrderByIdDesc();

    boolean existsByFactureNumberAndIdNot(String factureNumber, Integer id);
}

