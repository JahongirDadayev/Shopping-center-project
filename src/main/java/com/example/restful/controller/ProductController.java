package com.example.restful.controller;

import com.example.restful.entity.DbProduct;
import com.example.restful.payload.ProductDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public List<DbProduct> getProducts(){
        return productService.getProducts();
    }

    @GetMapping(path = "/category/{id}")
    public List<DbProduct> getCategoryProducts(@PathVariable Integer id){
        return productService.getCategoryProducts(id);
    }

    @PostMapping
    public Result postProduct(@RequestBody ProductDto productDto){
        return productService.postProduct(productDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto){
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteProduct(@PathVariable Integer id){
        return productService.deleteProduct(id);
    }
}
