package com.example.restful.entity;

import com.example.restful.status.InputStatus;
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
public class DbInput implements Cloneable{
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

    @Column(unique = true)
    private String factureNumber;

    private String code;

    @OneToMany(mappedBy = "dbInput")
    @JsonManagedReference
    private List<DbInputProduct> inputProductList;

    @Column(nullable = false)
    private InputStatus inputStatus;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbInput input = (DbInput) o;
        return Objects.equals(id, input.id) && Objects.equals(inputDate, input.inputDate) && Objects.equals(dbWarehouse, input.dbWarehouse) && Objects.equals(dbSupplier, input.dbSupplier) && Objects.equals(dbCurrency, input.dbCurrency) && Objects.equals(factureNumber, input.factureNumber) && Objects.equals(code, input.code) && Objects.equals(inputProductList, input.inputProductList) && inputStatus == input.inputStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inputDate, dbWarehouse, dbSupplier, dbCurrency, factureNumber, code, inputProductList, inputStatus);
    }
}
