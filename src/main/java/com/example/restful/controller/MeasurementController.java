package com.example.restful.controller;

import com.example.restful.entity.DbMeasurement;
import com.example.restful.payload.MeasurementDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/measurement")
public class MeasurementController {
    @Autowired
    MeasurementService measurementService;

    @GetMapping
    public List<DbMeasurement> getMeasurements(){
        return measurementService.getMeasurements();
    }

    @PostMapping
    public Result postMeasurement(@RequestBody MeasurementDto measurementDto){
        return measurementService.postMeasurement(measurementDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateMeasurement(@PathVariable Integer id, @RequestBody MeasurementDto measurementDto){
        return measurementService.updateMeasurement(id, measurementDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteMeasurement(@PathVariable Integer id){
        return measurementService.deleteMeasurement(id);
    }
}
