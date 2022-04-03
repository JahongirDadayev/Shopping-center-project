package com.example.restful.repository;

import com.example.restful.entity.DbClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<DbClient, Integer> {
    List<DbClient> findAllByActive(boolean active);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);
}
