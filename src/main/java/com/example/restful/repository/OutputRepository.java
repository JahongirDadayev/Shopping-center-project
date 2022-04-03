package com.example.restful.repository;

import com.example.restful.entity.DbOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OutputRepository extends JpaRepository<DbOutput, Integer> {
    Page<DbOutput> findAllByDbWarehouse_Id(Integer dbWarehouse_id, Pageable pageable);

    boolean existsByFactureNumber(String factureNumber);

    Optional<DbOutput> findTopByOrderByIdDesc();

    boolean existsByFactureNumberAndIdNot(String factureNumber, Integer id);
}
