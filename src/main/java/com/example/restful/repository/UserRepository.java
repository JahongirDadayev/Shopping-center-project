package com.example.restful.repository;

import com.example.restful.entity.DbUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<DbUser, Integer> {
    Page<DbUser> findAllByActive(boolean active, Pageable pageable);

    @Query(value = "SELECT db_user.* FROM db_user JOIN db_user_db_warehouses ON id = db_user_id WHERE db_warehouses_id=:warehouse_id AND active = :active", nativeQuery = true)
    Page<DbUser> findByWarehouse_Id(Integer warehouse_id, boolean active, Pageable pageable);

    boolean existsByEmail(String email);

    DbUser findTopByOrderByIdDesc();

    boolean existsByEmailAndIdNot(String email, Integer id);
}
