package com.example.restful.controller;

import com.example.restful.entity.DbWarehouse;
import com.example.restful.payload.WarehouseDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/warehouse")
public class WarehouseController {
    @Autowired
    WarehouseService warehouseService;

    @GetMapping
    public List<DbWarehouse> getWarehouses() {
        return warehouseService.getWarehouses();
    }

    @PostMapping
    public Result postWarehouse(@RequestBody WarehouseDto warehouseDto) {
        return warehouseService.postWarehouse(warehouseDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateWarehouse(@PathVariable Integer id, @RequestBody WarehouseDto warehouseDto) {
        return warehouseService.updateWarehouse(id, warehouseDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteWarehouse(@PathVariable Integer id) {
        return warehouseService.deleteWarehouse(id);
    }
}
