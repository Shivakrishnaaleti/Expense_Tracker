package com.example.expenseTracker.mapper;

import com.example.expenseTracker.dto.category.CategoryRequestDTO;
import com.example.expenseTracker.dto.category.CategoryResponseDTO;
import com.example.expenseTracker.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO categoryRequestDTO){

        Category category = new Category();

        category.setName(categoryRequestDTO.getName());

        return category;

    }

    public CategoryResponseDTO toResponse(Category category){

        return  new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }
}
