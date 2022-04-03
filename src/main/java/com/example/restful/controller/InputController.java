package com.example.restful.controller;

import com.example.restful.entity.DbInput;
import com.example.restful.payload.InputDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/input")
public class InputController {
    @Autowired
    InputService inputService;

    @GetMapping
    public List<DbInput> getInputs(@RequestParam(defaultValue = "1") Integer page){
        return inputService.getInputs(page);
    }

    @GetMapping(path = "/warehouse/{id}")
    public List<DbInput> getWarehouseInputs(@PathVariable Integer id, @RequestParam(defaultValue = "1") Integer page){
        return inputService.getWarehouseInputs(id, page);
    }

    @PostMapping
    public Result postInput(@RequestBody InputDto inputDto) throws CloneNotSupportedException {
        return inputService.postInput(inputDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateInput(@PathVariable Integer id, @RequestBody InputDto inputDto) throws CloneNotSupportedException {
        return inputService.updateInput(id, inputDto);
    }
}
