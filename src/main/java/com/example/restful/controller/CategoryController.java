package com.example.restful.controller;

import com.example.restful.entity.DbCategory;
import com.example.restful.payload.CategoryDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<DbCategory> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping(path = "/parentId/{parentId}")
    public List<DbCategory> getParentCategories(@PathVariable Integer parentId) {
        return categoryService.getParentCategories(parentId);
    }

    @PostMapping
    public Result postCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.postCategory(categoryDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(id, categoryDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteCategory(@PathVariable Integer id){
        return categoryService.deleteCategory(id);
    }
}
