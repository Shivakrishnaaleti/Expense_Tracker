package com.example.expenseTracker.controller;

import com.example.expenseTracker.dto.expense.ExpenseRequestDTO;
import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import com.example.expenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@Valid @RequestBody ExpenseRequestDTO request){

        ExpenseResponseDTO response = expenseService.createExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id){

        ExpenseResponseDTO response = expenseService.getExpenseById(id);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponseDTO>> getAllExpenses(Pageable pageable){

        Page<ExpenseResponseDTO> response = expenseService.getAllExpenses(pageable);

        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDTO request) {

        ExpenseResponseDTO response = expenseService.updateExpense(id, request);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> deleteExpense(@PathVariable Long id){

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();

    }
}
