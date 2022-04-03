package com.example.restful.controller;

import com.example.restful.entity.DbOutput;
import com.example.restful.payload.OutputDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/output")
public class OutputController {
    @Autowired
    OutputService outputService;

    @GetMapping
    public List<DbOutput> getOutputs(@RequestParam(defaultValue = "1") Integer page){
        return outputService.getOutputs(page);
    }

    @GetMapping(path = "/warehouse/{id}")
    public List<DbOutput> getWarehouseOutputs(@PathVariable Integer id, @RequestParam Integer page){
        return outputService.getWarehouseOutputs(id, page);
    }

    @PostMapping
    public Result postOutput(@RequestBody OutputDto outputDto) throws CloneNotSupportedException {
        return outputService.postOutput(outputDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateOutput(@PathVariable Integer id, @RequestBody OutputDto outputDto) throws CloneNotSupportedException {
        return outputService.updateOutput(id, outputDto);
    }
}
