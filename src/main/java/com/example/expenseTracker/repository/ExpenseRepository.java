package com.example.expenseTracker.repository;

import com.example.expenseTracker.entity.Category;
import com.example.expenseTracker.entity.Expense;
import com.example.expenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    boolean existsByTitleAndAmountAndCategoryAndExpenseDateAndUser(
            String title,
            BigDecimal amount,
            Category category,
            LocalDate expenseDate,
            User user
    );
}
