package com.example.restful.service;

import com.example.restful.entity.*;
import com.example.restful.payload.OutputDto;
import com.example.restful.payload.OutputProductDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OutputService {
    @Autowired
    OutputRepository outputRepository;

    @Autowired
    OutputProductRepository outputProductRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    ProductRepository productRepository;

    public List<DbOutput> getOutputs(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return outputRepository.findAll(pageable).getContent();
    }

    public List<DbOutput> getWarehouseOutputs(Integer id, Integer page) {
        if (warehouseRepository.existsById(id)) {
            Pageable pageable = PageRequest.of(page - 1, 10);
            return outputRepository.findAllByDbWarehouse_Id(id, pageable).getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Result postOutput(OutputDto outputDto) throws CloneNotSupportedException {
        if (!outputRepository.existsByFactureNumber(outputDto.getFactureNumber())) {
            DbOutput output = new DbOutput();
            List<DbOutputProduct> outputProductList = new ArrayList<>();
            return saveOutputInformation(output, outputProductList, outputDto, "Saved output information");
        } else {
            return new Result("There is an output that you enter", false);
        }
    }

    public Result updateOutput(Integer id, OutputDto outputDto) throws CloneNotSupportedException {
        Optional<DbOutput> optionalDbOutput = outputRepository.findById(id);
        if (optionalDbOutput.isPresent()) {
            if (outputRepository.existsByFactureNumberAndIdNot(outputDto.getFactureNumber(), id)) {
                DbOutput output = optionalDbOutput.get();
                List<DbOutputProduct> outputProductList = outputProductRepository.findAllByDbOutput_Id(id);
                return saveOutputInformation(output, outputProductList, outputDto, "Update output information");
            } else {
                return new Result("There is an output that you enter", false);
            }
        } else {
            return new Result("No output matching the id you entered", false);
        }
    }

    private Result saveOutputInformation(DbOutput output, List<DbOutputProduct> outputProductList, OutputDto outputDto, String text) throws CloneNotSupportedException {
        Optional<DbWarehouse> optionalDbWarehouse = warehouseRepository.findById(outputDto.getWarehouseId());
        if (optionalDbWarehouse.isPresent()) {
            Optional<DbClient> optionalDbClient = clientRepository.findById(outputDto.getClientId());
            if (optionalDbClient.isPresent()) {
                Optional<DbCurrency> optionalDbCurrency = currencyRepository.findById(outputDto.getCurrencyId());
                if (optionalDbCurrency.isPresent()) {
                    List<DbProduct> productList = checkOutputProductsInformation(outputDto.getOutputProductDtoList());
                    if (productList != null) {
                        DbOutput oldOutput = new DbOutput();
                        if (!output.equals(oldOutput)) {
                            oldOutput = (DbOutput) output.clone();
                        }
                        output.setOutputDate(new Timestamp(System.currentTimeMillis()));
                        output.setDbWarehouse(optionalDbWarehouse.get());
                        output.setDbClient(optionalDbClient.get());
                        output.setDbCurrency(optionalDbCurrency.get());
                        output.setFactureNumber(outputDto.getFactureNumber());
                        output.setCode(generationCode());
                        output.setOutputStatus(outputDto.getOutputStatus());
                        try {
                            outputRepository.save(output);
                            List<DbOutputProduct> oldInputProductList = new ArrayList<>(outputProductList);
                            for (int i = 0; i < outputDto.getOutputProductDtoList().size(); i++) {
                                if (outputProductList.size() <= i) {
                                    outputProductList.add(new DbOutputProduct());
                                }
                                DbOutputProduct outputProduct = outputProductList.get(i);
                                OutputProductDto outputProductDto = outputDto.getOutputProductDtoList().get(i);
                                outputProduct.setDbProduct(productList.get(i));
                                outputProduct.setAmount(outputProductDto.getAmount());
                                outputProduct.setPrice(outputProductDto.getPrice());
                                outputProduct.setDbOutput(output);
                                try {
                                    outputProductRepository.save(outputProduct);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (!oldOutput.equals(new DbOutput())) {
                                        outputRepository.save(oldOutput);
                                        for (int j = 0; j < outputProductList.size(); j++) {
                                            if (j < oldInputProductList.size()) {
                                                outputProductRepository.save(oldInputProductList.get(j));
                                            } else {
                                                outputProductRepository.delete(outputProductList.get(j));
                                            }
                                        }
                                    } else {
                                        outputRepository.delete(output);
                                        outputProductRepository.deleteAll(outputProductList);
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
                return new Result("No client matching the id you entered", false);
            }
        } else {
            return new Result("No warehouse matching the id you entered", false);
        }
    }

    private String generationCode() {
        Optional<DbOutput> optionalDbOutput = outputRepository.findTopByOrderByIdDesc();
        if (optionalDbOutput.isPresent()) {
            DbOutput output = optionalDbOutput.get();
            return String.valueOf(Integer.parseInt(output.getCode()) + 1);
        } else {
            return "1";
        }
    }

    private List<DbProduct> checkOutputProductsInformation(List<OutputProductDto> outputProductDtoList) {
        List<DbProduct> productList = new ArrayList<>();
        for (OutputProductDto outputProductDto : outputProductDtoList) {
            Optional<DbProduct> optionalDbProduct = productRepository.findById(outputProductDto.getProductId());
            if (!optionalDbProduct.isPresent()) {
                return null;
            }
            productList.add(optionalDbProduct.get());
        }
        return productList;
    }
}
