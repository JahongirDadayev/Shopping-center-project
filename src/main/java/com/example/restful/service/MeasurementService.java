package com.example.restful.service;

import com.example.restful.entity.DbMeasurement;
import com.example.restful.payload.MeasurementDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {
    @Autowired
    MeasurementRepository measurementRepository;

    public List<DbMeasurement> getMeasurements() {
        return measurementRepository.findAll();
    }

    public Result postMeasurement(MeasurementDto measurementDto) {
        if (!measurementRepository.existsByName(measurementDto.getName())){
            DbMeasurement measurement = new DbMeasurement();
            return saveMeasurementInformation(measurement, measurementDto, "Saved measurement information");
        }else {
            return new Result("There is such a measurement", false);
        }
    }

    public Result updateMeasurement(Integer id, MeasurementDto measurementDto){
        Optional<DbMeasurement> optionalDbMeasurement = measurementRepository.findById(id);
        if (optionalDbMeasurement.isPresent()){
            if (!measurementRepository.existsByNameAndIdNot(measurementDto.getName(), id)){
                DbMeasurement measurement = optionalDbMeasurement.get();
                return saveMeasurementInformation(measurement, measurementDto, "Update measurement information");
            }else {
                return new Result("There is such a measurement", false);
            }
        }else {
            return new Result("Could not find measurement that matches the id you entered", false);
        }
    }

    public Result deleteMeasurement(Integer id) {
        if (measurementRepository.existsById(id)){
            try {
                measurementRepository.deleteById(id);
                return new Result("Delete measurement information", true);
            }catch (Exception e){
                e.printStackTrace();
                return new Result("The ids of this category may have been used in other tables. Therefore, the card was not deleted", false);
            }
        }else {
            return new Result("Could not find measurement that matches the id you entered", false);
        }
    }

    private Result saveMeasurementInformation(DbMeasurement measurement, MeasurementDto measurementDto, String text){
        measurement.setName(measurementDto.getName());
        return saveMeasurement(measurement, text);
    }

    private Result saveMeasurement(DbMeasurement measurement, String text){
        try {
            measurementRepository.save(measurement);
            return new Result(text, true);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("Error dbMeasurement table", false);
        }
    }
}
