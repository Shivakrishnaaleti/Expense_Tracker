package com.example.expenseTracker.service;

import com.example.expenseTracker.entity.Category;
import com.example.expenseTracker.exception.DuplicateResourceException;
import com.example.expenseTracker.exception.ResourceNotFoundException;
import com.example.expenseTracker.dto.expense.ExpenseRequestDTO;
import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import com.example.expenseTracker.entity.Expense;
import com.example.expenseTracker.mapper.ExpenseMapper;
import com.example.expenseTracker.repository.CategoryRepository;
import com.example.expenseTracker.repository.ExpenseRepository;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponseDTO createExpense(ExpenseRequestDTO request){

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+request.getUserId()));

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id "+request.getCategoryId()));

        boolean exists = expenseRepository.existsByTitleAndAmountAndCategoryAndExpenseDateAndUser(
                request.getTitle(),
                request.getAmount(),
                category,
                request.getExpenseDate(),
                user
        );

        if(exists){
            throw new DuplicateResourceException("Expense already exists");
        }

        Expense expense = expenseMapper.toEntity(request);

        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);

        return expenseMapper.toResponse(savedExpense);

    }

    public ExpenseResponseDTO getExpenseById(Long id){

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id "+id));

        return expenseMapper.toResponse(expense);

    }

    public Page<ExpenseResponseDTO> getAllExpenses(Pageable pageable){

        Page<Expense> expenses = expenseRepository.findAll(pageable);

        return expenses.map(expenseMapper::toResponse);

    }

    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO request){

        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id "+id));

        User user = userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+request.getUserId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->new ResourceNotFoundException("Category not found with id "+request.getCategoryId()));
        existingExpense.setTitle(request.getTitle());
        existingExpense.setAmount(request.getAmount());
        existingExpense.setExpenseDate(request.getExpenseDate());
        existingExpense.setCategory(category);
        existingExpense.setUser(user);

        Expense updatedExpense = expenseRepository.save(existingExpense);

        return expenseMapper.toResponse(updatedExpense);

    }

    public void deleteExpense(Long id){
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id "+id));

        expenseRepository.delete(expense);
    }
}
