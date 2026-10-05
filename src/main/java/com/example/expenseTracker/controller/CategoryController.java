package com.example.expenseTracker.controller;


import com.example.expenseTracker.dto.category.CategoryRequestDTO;
import com.example.expenseTracker.dto.category.CategoryResponseDTO;
import com.example.expenseTracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO){

        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id){

        CategoryResponseDTO responseDTO = categoryService.getCategory(id);

        return ResponseEntity
                .ok(responseDTO);

    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(Pageable pageable){

        Page<CategoryResponseDTO> responseDTO = categoryService.getAllCategories(pageable);

        return ResponseEntity
                .ok(responseDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @PathVariable Long id, @RequestBody CategoryRequestDTO requestDTO){

        CategoryResponseDTO responseDTO = categoryService.updateCategory(id, requestDTO);

        return ResponseEntity
                .ok(responseDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable Long id){

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();

    }
}
