package com.example.restful.service;

import com.example.restful.entity.DbAttachment;
import com.example.restful.entity.DbCategory;
import com.example.restful.entity.DbMeasurement;
import com.example.restful.entity.DbProduct;
import com.example.restful.payload.ProductDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.AttachmentRepository;
import com.example.restful.repository.CategoryRepository;
import com.example.restful.repository.MeasurementRepository;
import com.example.restful.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    public List<DbProduct> getProducts() {
        return productRepository.findAll();
    }

    public List<DbProduct> getCategoryProducts(Integer id) {
        if (categoryRepository.existsById(id)) {
            return productRepository.findAllByDbCategory_Id(id);
        } else {
            return new ArrayList<>();
        }
    }

    public Result postProduct(ProductDto productDto) {
        if (!productRepository.existsByName(productDto.getName())) {
            DbProduct product = new DbProduct();
            return saveProductInformation(product, productDto, "Saved product information");
        } else {
            return new Result("There is a product with this name", false);
        }
    }

    public Result updateProduct(Integer id, ProductDto productDto) {
        Optional<DbProduct> optionalDbProduct = productRepository.findById(id);
        if (optionalDbProduct.isPresent()) {
            if (!productRepository.existsByNameAndIdNot(productDto.getName(), id)) {
                DbProduct product = optionalDbProduct.get();
                return saveProductInformation(product, productDto, "Update product information");
            } else {
                return new Result("There is a product with this name", false);
            }
        } else {
            return new Result("No product matching the id you entered", false);
        }
    }

    public Result deleteProduct(Integer id) {
        if (productRepository.existsById(id)){
            try {
                productRepository.deleteById(id);
                return new Result("Delete product information", true);
            }catch (Exception e){
                e.printStackTrace();
                return new Result("Error dbProduct table", false);
            }
        }else {
            return new Result("No product matching the id you entered", false);
        }
    }

    private Result saveProductInformation(DbProduct product, ProductDto productDto, String text) {
        Optional<DbCategory> optionalDbCategory = categoryRepository.findById(productDto.getCategoryId());
        if (optionalDbCategory.isPresent()) {
            Optional<DbMeasurement> optionalDbMeasurement = measurementRepository.findById(productDto.getMeasurementId());
            if (optionalDbMeasurement.isPresent()) {
                List<DbAttachment> attachmentList = new ArrayList<>();
                for (Integer attachmentId : productDto.getAttachmentsId()) {
                    Optional<DbAttachment> optionalDbAttachment = attachmentRepository.findById(attachmentId);
                    if (optionalDbAttachment.isPresent()) {
                        attachmentList.add(optionalDbAttachment.get());
                    } else {
                        return new Result("You have added an existing non-existent attachment", false);
                    }
                }
                product.setName(productDto.getName());
                product.setDbCategory(optionalDbCategory.get());
                product.setDbAttachments(attachmentList);
                if (product.getQrcode() == null) {
                    product.setQrcode(generationCode());
                }
                product.setDbMeasurement(optionalDbMeasurement.get());
                try {
                    productRepository.save(product);
                    return new Result(text, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result("Error dbProduct table", false);
                }
            } else {
                return new Result("You have entered a measurement that does not exist", false);
            }
        } else {
            return new Result("You have entered a category that does not exist", false);
        }
    }

    private String generationCode() {
        Optional<DbProduct> optionalDbProduct = productRepository.findTopByOrderByIdDesc();
        if (optionalDbProduct.isPresent()) {
            DbProduct product = optionalDbProduct.get();
            return String.valueOf(Integer.parseInt(product.getQrcode()) + 1);
        } else {
            return "1";
        }
    }
}
