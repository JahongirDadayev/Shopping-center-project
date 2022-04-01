package com.example.restful.service;

import com.example.restful.entity.DbCategory;
import com.example.restful.payload.CategoryDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<DbCategory> getCategories() {
        return categoryRepository.findAll();
    }

    public List<DbCategory> getParentCategories(Integer parentId) {
        if (categoryRepository.existsById(parentId)) {
            return categoryRepository.findAllByParentCategory_Id(parentId);
        } else {
            return new ArrayList<>();
        }
    }

    public Result postCategory(CategoryDto categoryDto) {
        if (!categoryRepository.existsByNameAndParentCategory_Id(categoryDto.getName(), categoryDto.getParentCategoryId())) {
            DbCategory category = new DbCategory();
            return saveCategoryInformation(category, categoryDto, "Saved category information");
        } else {
            return new Result("There is such a category", false);
        }
    }

    public Result updateCategory(Integer id, CategoryDto categoryDto) {
        Optional<DbCategory> optionalDbCategory = categoryRepository.findById(id);
        if (optionalDbCategory.isPresent()) {
            if (!categoryRepository.existsByNameAndParentCategory_IdAndIdNot(categoryDto.getName(), categoryDto.getParentCategoryId(), id)) {
                DbCategory category = optionalDbCategory.get();
                return saveCategoryInformation(category, categoryDto, "Update category information");
            } else {
                return new Result("There is such a category", false);
            }
        } else {
            return new Result("Could not find category that matches the id you entered", false);
        }
    }

    public Result deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            try {
                categoryRepository.deleteById(id);
                return new Result("Delete category information", true);
            } catch (Exception e) {
                return new Result("The ids of this category may have been used in other tables. Therefore, the card was not deleted", false);
            }
        } else {
            return new Result("Could not find category that matches the id you entered", false);
        }
    }

    private Result saveCategoryInformation(DbCategory category, CategoryDto categoryDto, String text) {
        category.setName(categoryDto.getName());
        if (categoryDto.getParentCategoryId() != null) {
            Optional<DbCategory> optionalParentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if (optionalParentCategory.isPresent()) {
                DbCategory parentCategory = optionalParentCategory.get();
                category.setParentCategory(parentCategory);
            } else {
                return new Result("No parentCategory matching the id you entered", false);
            }
        } else {
            category.setParentCategory(null);
        }
        return saveCategory(category, text);
    }

    private Result saveCategory(DbCategory category, String text) {
        try {
            categoryRepository.save(category);
            return new Result(text, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error dbCategory table", false);
        }
    }
}
