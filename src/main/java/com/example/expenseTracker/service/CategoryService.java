package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.category.CategoryRequestDTO;
import com.example.expenseTracker.dto.category.CategoryResponseDTO;
import com.example.expenseTracker.entity.Category;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.exception.DuplicateResourceException;
import com.example.expenseTracker.exception.ResourceNotFoundException;
import com.example.expenseTracker.mapper.CategoryMapper;
import com.example.expenseTracker.repository.CategoryRepository;
import com.example.expenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO){

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found at id "+requestDTO.getUserId()));

        categoryRepository.findByNameAndUserId(
                        requestDTO.getName(),
                        requestDTO.getUserId()
                )
                .ifPresent(category -> {
                    throw new DuplicateResourceException(
                            "Category already exists with name " + requestDTO.getName()
                    );
                });

        Category category = categoryMapper.toEntity(requestDTO);

        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);

    }

    public CategoryResponseDTO getCategory(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id "+id));

        return categoryMapper.toResponse(category);

    }

    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable){

        Page<Category> categories = categoryRepository.findAll(pageable);

        return categories.map(categoryMapper :: toResponse);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO){

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id "+id));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+requestDTO.getUserId()));

        categoryRepository.findByNameAndUserId(
                        requestDTO.getName(),
                        requestDTO.getUserId()
                )
                .ifPresent(category -> {

                    if (!category.getId().equals(id)) {
                        throw new DuplicateResourceException(
                                "Category already exists with name " + requestDTO.getName());
                    }
                });

        existingCategory.setName(requestDTO.getName());
        existingCategory.setUser(user);

        Category updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.toResponse(updatedCategory);

    }

    public void deleteCategory(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id "+id));

        categoryRepository.delete(category);

    }
}
