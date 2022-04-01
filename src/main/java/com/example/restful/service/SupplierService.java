package com.example.restful.service;

import com.example.restful.entity.DbSupplier;
import com.example.restful.payload.SupplierDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    SupplierRepository supplierRepository;

    public List<DbSupplier> getSuppliers() {
        return supplierRepository.findAllByActive(true);
    }

    public Result postSupplier(SupplierDto supplierDto) {
        if (!supplierRepository.existsByPhoneNumber(supplierDto.getPhoneNumber())){
            DbSupplier supplier = new DbSupplier();
            return saveSupplierInformation(supplier, supplierDto, "Saved supplier information");
        }else {
            return new Result("Such a supplier is available", false);
        }
    }

    public Result updateSupplier(Integer id, SupplierDto supplierDto) {
        Optional<DbSupplier> optionalDbSupplier = supplierRepository.findById(id);
        if (optionalDbSupplier.isPresent() && optionalDbSupplier.get().isActive()){
            if (!supplierRepository.existsByPhoneNumberAndIdNot(supplierDto.getPhoneNumber(), id)){
                DbSupplier supplier = optionalDbSupplier.get();
                return saveSupplierInformation(supplier, supplierDto, "Update supplier information");
            }else {
                return new Result("Such a supplier is available", false);
            }
        }else {
            return new Result("No supplier matching the id you entered", false);
        }
    }

    public Result deleteSupplier(Integer id) {
        Optional<DbSupplier> optionalDbSupplier = supplierRepository.findById(id);
        if (optionalDbSupplier.isPresent() && optionalDbSupplier.get().isActive()){
            DbSupplier supplier = optionalDbSupplier.get();
            supplier.setActive(false);
            return saveSupplier(supplier, "Delete supplier information");
        }else {
            return new Result("No supplier matching the id you entered", false);
        }
    }

    private Result saveSupplierInformation(DbSupplier supplier, SupplierDto supplierDto, String text){
        supplier.setName(supplierDto.getName());
        supplier.setPhoneNumber(supplierDto.getPhoneNumber());
        return saveSupplier(supplier, text);
    }

    private Result saveSupplier(DbSupplier supplier, String text){
        try {
            supplierRepository.save(supplier);
            return new Result(text, true);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("Error dbSupplier table", false);
        }
    }
}
