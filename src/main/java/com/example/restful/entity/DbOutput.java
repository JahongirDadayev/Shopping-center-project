package com.example.restful.entity;

import com.example.restful.status.OutputStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DbOutput implements Cloneable{
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

    @OneToMany(mappedBy = "dbOutput")
    @JsonManagedReference
    List<DbOutputProduct> productList;

    @Column(nullable = false)
    private OutputStatus outputStatus;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbOutput dbOutput = (DbOutput) o;
        return Objects.equals(id, dbOutput.id) && Objects.equals(outputDate, dbOutput.outputDate) && Objects.equals(dbWarehouse, dbOutput.dbWarehouse) && Objects.equals(dbClient, dbOutput.dbClient) && Objects.equals(dbCurrency, dbOutput.dbCurrency) && Objects.equals(factureNumber, dbOutput.factureNumber) && Objects.equals(code, dbOutput.code) && Objects.equals(productList, dbOutput.productList) && outputStatus == dbOutput.outputStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outputDate, dbWarehouse, dbClient, dbCurrency, factureNumber, code, productList, outputStatus);
    }
}
