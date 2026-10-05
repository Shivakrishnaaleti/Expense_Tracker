package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.expense.ExpenseRequestDTO;
import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import com.example.expenseTracker.entity.Category;
import com.example.expenseTracker.entity.Expense;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.exception.DuplicateResourceException;
import com.example.expenseTracker.exception.ResourceNotFoundException;
import com.example.expenseTracker.mapper.ExpenseMapper;
import com.example.expenseTracker.repository.CategoryRepository;
import com.example.expenseTracker.repository.ExpenseRepository;
import com.example.expenseTracker.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseService expenseService;


    @Test
    void createExpense_shouldCreateSuccessfully(){

        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setCategoryId(10L);
        requestDTO.setTitle("Food");
        requestDTO.setAmount(new BigDecimal("500"));
        requestDTO.setExpenseDate(LocalDate.of(2026,9,4));

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(10L);

        Expense expense = new Expense();

        Expense savedExpense = new Expense();

        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));

        when(expenseRepository.existsByTitleAndAmountAndCategoryAndExpenseDateAndUser(
                requestDTO.getTitle(),
                requestDTO.getAmount(),
                category,
                requestDTO.getExpenseDate(),
                user
        )).thenReturn(false);

        when(expenseMapper.toEntity(requestDTO))
                .thenReturn(expense);

        when(expenseRepository.save(expense))
                .thenReturn(savedExpense);
        when(expenseMapper.toResponse(savedExpense))
                .thenReturn(responseDTO);

        ExpenseResponseDTO result = expenseService.createExpense(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(userRepository).findById(1L);
        verify(expenseRepository).save(expense);
        verify(expenseMapper).toEntity(requestDTO);
        verify(expenseMapper).toResponse(savedExpense);

    }

    @Test
    void createExpense_throwExceptionExists(){

        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO();
        requestDTO.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,() -> expenseService.createExpense(requestDTO)
        );

        assertTrue(exception.getMessage().contains("User not found with id "));

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(10L);

        requestDTO.setTitle("food");
        requestDTO.setAmount(new BigDecimal("500"));
        requestDTO.setCategoryId(10L);
        requestDTO.setExpenseDate(LocalDate.of(2026,9,4));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));

        when(expenseRepository.existsByTitleAndAmountAndCategoryAndExpenseDateAndUser(
                requestDTO.getTitle(),
                requestDTO.getAmount(),
                category,
                requestDTO.getExpenseDate(),
                user
        )).thenReturn(true);

        DuplicateResourceException expenseException = assertThrows(
                DuplicateResourceException.class,
                () -> expenseService.createExpense(requestDTO)
        );

        assertEquals("Expense already exists", expenseException.getMessage());

        verify(expenseRepository, never()).save(any());
        verify(expenseMapper, never()).toEntity(requestDTO);



    }

    @Test
    void sampleTest(){
        int a = 2+3;
        Assertions.assertEquals(a,5);
    }
}