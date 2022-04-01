package com.example.restful.service;

import com.example.restful.entity.DbWarehouse;
import com.example.restful.payload.WarehouseDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;

    public List<DbWarehouse> getWarehouses() {
        return warehouseRepository.findAllByActive(true);
    }

    public Result postWarehouse(WarehouseDto warehouseDto) {
        if (!warehouseRepository.existsByName(warehouseDto.getName())) {
            DbWarehouse warehouse = new DbWarehouse();
            warehouse.setName(warehouseDto.getName());
            return saveWarehouse(warehouse, "Saved warehouse information");
        } else {
            return new Result("The name you entered was used for another warehouse", false);
        }
    }

    public Result updateWarehouse(Integer id, WarehouseDto warehouseDto) {
        Optional<DbWarehouse> optionalDbWarehouse = warehouseRepository.findById(id);
        if (optionalDbWarehouse.isPresent() && optionalDbWarehouse.get().isActive()) {
            if (!warehouseRepository.existsByNameAndIdNot(warehouseDto.getName(), id)) {
                DbWarehouse warehouse = optionalDbWarehouse.get();
                warehouse.setName(warehouseDto.getName());
                return saveWarehouse(warehouse, "Update warehouse information");
            } else {
                return new Result("The name you entered was used for another warehouse", false);
            }
        } else {
            return new Result("No warehouse matching the id you entered", false);
        }
    }

    public Result deleteWarehouse(Integer id) {
        Optional<DbWarehouse> optionalDbWarehouse = warehouseRepository.findById(id);
        if (optionalDbWarehouse.isPresent() && optionalDbWarehouse.get().isActive()) {
            DbWarehouse warehouse = optionalDbWarehouse.get();
            warehouse.setActive(false);
            return saveWarehouse(warehouse, "Delete warehouse information");
        } else {
            return new Result("No warehouse matching the id you entered", false);
        }
    }

    private Result saveWarehouse(DbWarehouse warehouse, String text) {
        try {
            warehouseRepository.save(warehouse);
            return new Result(text, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error dbWarehouse table", false);
        }
    }
}
