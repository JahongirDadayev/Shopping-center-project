package com.example.restful.repository;

import com.example.restful.entity.DbSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<DbSupplier, Integer> {
    List<DbSupplier> findAllByActive(boolean active);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);
}
