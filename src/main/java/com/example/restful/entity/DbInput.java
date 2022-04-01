package com.example.restful.entity;

import com.example.restful.status.InputStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DbInput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Timestamp inputDate;

    @ManyToOne(optional = false)
    private DbWarehouse dbWarehouse;

    @ManyToOne(optional = false)
    private DbSupplier dbSupplier;

    @ManyToOne(optional = false)
    private DbCurrency dbCurrency;

    private String factureNumber;

    private String code;

    @Column(nullable = false)
    private InputStatus inputStatus;
}
