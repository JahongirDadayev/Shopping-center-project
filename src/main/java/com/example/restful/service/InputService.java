package com.example.restful.service;

import com.example.restful.entity.*;
import com.example.restful.payload.InputDto;
import com.example.restful.payload.InputProductDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InputService {
    @Autowired
    InputRepository inputRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    InputProductRepository inputProductRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    ProductRepository productRepository;

    public List<DbInput> getInputs(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return inputRepository.findAll(pageable).getContent();
    }

    public List<DbInput> getWarehouseInputs(Integer id, Integer page) {
        if (warehouseRepository.existsById(id)) {
            Pageable pageable = PageRequest.of(page - 1, 10);
            return inputRepository.findAllByDbWarehouse_Id(id, pageable).getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Result postInput(InputDto inputDto) throws CloneNotSupportedException {
        if (!inputRepository.existsByFactureNumber(inputDto.getFactureNumber())) {
            DbInput input = new DbInput();
            List<DbInputProduct> inputProductList = new ArrayList<>();
            return saveInputInformation(input, inputProductList, inputDto, "Saved input information");
        } else {
            return new Result("There is an input you are entering", false);
        }
    }

    public Result updateInput(Integer id, InputDto inputDto) throws CloneNotSupportedException {
        Optional<DbInput> optionalDbInput = inputRepository.findById(id);
        if (optionalDbInput.isPresent()) {
            if (!inputRepository.existsByFactureNumberAndIdNot(inputDto.getFactureNumber(), id)) {
                DbInput input = optionalDbInput.get();
                List<DbInputProduct> inputProductList = inputProductRepository.findAllByDbInput_Id(id);
                return saveInputInformation(input, inputProductList, inputDto, "Update input information");
            } else {
                return new Result("There is an input you are entering", false);
            }
        } else {
            return new Result("The input that matches the id you entered is found", false);
        }
    }

    private Result saveInputInformation(DbInput input, List<DbInputProduct> inputProductList, InputDto inputDto, String text) throws CloneNotSupportedException {
        Optional<DbWarehouse> optionalDbWarehouse = warehouseRepository.findById(inputDto.getWarehouseId());
        if (optionalDbWarehouse.isPresent()) {
            Optional<DbSupplier> optionalDbSupplier = supplierRepository.findById(inputDto.getSupplierId());
            if (optionalDbSupplier.isPresent()) {
                Optional<DbCurrency> optionalDbCurrency = currencyRepository.findById(inputDto.getCurrencyId());
                if (optionalDbCurrency.isPresent()) {
                    List<DbProduct> productList = checkInputProductsInformation(inputDto.getInputProductDtoList());
                    if (productList != null) {
                        DbInput oldInput = new DbInput();
                        if (!input.equals(oldInput)) {
                            oldInput = (DbInput) input.clone();
                        }
                        input.setInputDate(new Timestamp(System.currentTimeMillis()));
                        input.setDbWarehouse(optionalDbWarehouse.get());
                        input.setDbSupplier(optionalDbSupplier.get());
                        input.setDbCurrency(optionalDbCurrency.get());
                        input.setFactureNumber(inputDto.getFactureNumber());
                        input.setCode(generationCode());
                        input.setInputStatus(inputDto.getInputStatus());
                        try {
                            inputRepository.save(input);
                            List<DbInputProduct> oldInputProductList = new ArrayList<>(inputProductList);
                            for (int i = 0; i < inputDto.getInputProductDtoList().size(); i++) {
                                if (inputProductList.size() <= i) {
                                    inputProductList.add(new DbInputProduct());
                                }
                                DbInputProduct inputProduct = inputProductList.get(i);
                                InputProductDto inputProductDto = inputDto.getInputProductDtoList().get(i);
                                inputProduct.setDbProduct(productList.get(i));
                                inputProduct.setAmount(inputProductDto.getAmount());
                                inputProduct.setPrice(inputProductDto.getPrice());
                                inputProduct.setExpireDate(LocalDate.parse(inputProductDto.getExpireDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                                inputProduct.setDbInput(input);
                                try {
                                    inputProductRepository.save(inputProduct);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (!oldInput.equals(new DbInput())) {
                                        inputRepository.save(oldInput);
                                        for (int j = 0; j < inputProductList.size(); j++) {
                                            if (j < oldInputProductList.size()) {
                                                inputProductRepository.save(oldInputProductList.get(j));
                                            } else {
                                                inputProductRepository.delete(inputProductList.get(j));
                                            }
                                        }
                                    } else {
                                        inputRepository.delete(input);
                                        inputProductRepository.deleteAll(inputProductList);
                                    }
                                    return new Result("Error dbInputProduct table", false);
                                }
                            }
                            return new Result(text, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Result("Error dbInput table", false);
                        }
                    } else {
                        return new Result("No product matching the id you entered", false);
                    }
                } else {
                    return new Result("No currency matching the id you entered", false);
                }
            } else {
                return new Result("No supplier matching the id you entered", false);
            }
        } else {
            return new Result("No warehouse matching the id you entered", false);
        }
    }

    private String generationCode() {
        Optional<DbInput> optionalDbInput = inputRepository.findTopByOrderByIdDesc();
        if (optionalDbInput.isPresent()) {
            DbInput input = optionalDbInput.get();
            return String.valueOf(Integer.parseInt(input.getCode()) + 1);
        } else {
            return "1";
        }
    }

    private List<DbProduct> checkInputProductsInformation(List<InputProductDto> inputProductDtoList) {
        List<DbProduct> productList = new ArrayList<>();
        for (InputProductDto inputProductDto : inputProductDtoList) {
            Optional<DbProduct> optionalDbProduct = productRepository.findById(inputProductDto.getProductId());
            if (!optionalDbProduct.isPresent()) {
                return null;
            }
            productList.add(optionalDbProduct.get());
        }
        return productList;
    }
}
