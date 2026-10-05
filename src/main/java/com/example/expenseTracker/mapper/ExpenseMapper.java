package com.example.expenseTracker.mapper;

import com.example.expenseTracker.dto.expense.ExpenseRequestDTO;
import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import com.example.expenseTracker.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {


    public Expense toEntity(ExpenseRequestDTO request){

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());

        return expense;
    }

    public ExpenseResponseDTO toResponse(Expense expense){

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getUser().getId(),
                expense.getCategory().getId(),
                expense.getCategory().getName()
        );
    }
}
