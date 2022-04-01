package com.example.restful.controller;

import com.example.restful.entity.DbCurrency;
import com.example.restful.payload.CurrencyDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @GetMapping
    public List<DbCurrency> getCurrencies(){
        return currencyService.getCurrencies();
    }

    @PostMapping
    public Result postCurrency(@RequestBody CurrencyDto currencyDto){
        return currencyService.postCurrency(currencyDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateCurrency(@PathVariable Integer id, @RequestBody CurrencyDto currencyDto){
        return currencyService.updateCurrency(id, currencyDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteCurrency(@PathVariable Integer id){
        return currencyService.deleteCurrency(id);
    }
}
