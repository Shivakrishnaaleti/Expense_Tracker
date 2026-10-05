package com.example.expenseTracker.repository;

import com.example.expenseTracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndUserId(String name, Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
