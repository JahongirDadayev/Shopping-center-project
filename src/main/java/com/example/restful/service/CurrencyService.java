package com.example.restful.service;

import com.example.restful.entity.DbCurrency;
import com.example.restful.payload.CurrencyDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;

    public List<DbCurrency> getCurrencies() {
        return currencyRepository.findAll();
    }

    public Result postCurrency(CurrencyDto currencyDto) {
        if (!currencyRepository.existsByName(currencyDto.getName())) {
            DbCurrency currency = new DbCurrency();
            return saveCurrencyInformation(currency, currencyDto, "Saved currency information");
        } else {
            return new Result("There is such a category", false);
        }
    }

    public Result updateCurrency(Integer id, CurrencyDto currencyDto) {
        Optional<DbCurrency> optionalDbCurrency = currencyRepository.findById(id);
        if (optionalDbCurrency.isPresent()) {
            if (!currencyRepository.existsByNameAndIdNot(currencyDto.getName(), id)) {
                DbCurrency currency = optionalDbCurrency.get();
                return saveCurrencyInformation(currency, currencyDto, "Update currency information");
            } else {
                return new Result("There is such a category", false);
            }
        } else {
            return new Result("No currency matching the id you entered was found", false);
        }
    }

    public Result deleteCurrency(Integer id) {
        if (currencyRepository.existsById(id)) {
            try {
                currencyRepository.deleteById(id);
                return new Result("Delete currency information", true);
            }catch (Exception e){
                e.printStackTrace();
                return new Result("The ids of this category may have been used in other tables. Therefore, the card was not deleted", false);
            }
        } else {
            return new Result("No currency matching the id you entered was found", false);
        }
    }

    private Result saveCurrencyInformation(DbCurrency currency, CurrencyDto currencyDto, String text) {
        currency.setName(currencyDto.getName());
        return saveCurrency(currency, text);
    }

    private Result saveCurrency(DbCurrency currency, String text) {
        try {
            currencyRepository.save(currency);
            return new Result(text, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error dbCurrency table", false);
        }
    }
}
