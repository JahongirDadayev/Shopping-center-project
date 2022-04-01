package com.example.restful.entity;

import com.example.restful.status.OutputStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DbOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Timestamp outputDate;

    @ManyToOne(optional = false)
    private DbWarehouse dbWarehouse;

    @ManyToOne(optional = false)
    private DbClient dbClient;

    @ManyToOne(optional = false)
    private DbCurrency dbCurrency;

    private String factureNumber;

    private String code;

    @Column(nullable = false)
    private OutputStatus outputStatus;
}
