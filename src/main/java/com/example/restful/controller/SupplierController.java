package com.example.restful.controller;

import com.example.restful.entity.DbSupplier;
import com.example.restful.payload.SupplierDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/supplier")
public class SupplierController {
    @Autowired
    SupplierService supplierService;

    @GetMapping
    public List<DbSupplier> getSuppliers(){
        return supplierService.getSuppliers();
    }

    @PostMapping
    public Result postSupplier(@RequestBody SupplierDto supplierDto){
        return supplierService.postSupplier(supplierDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateSupplier(@PathVariable Integer id, @RequestBody SupplierDto supplierDto){
        return supplierService.updateSupplier(id, supplierDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteSupplier(@PathVariable Integer id){
        return supplierService.deleteSupplier(id);
    }
}
