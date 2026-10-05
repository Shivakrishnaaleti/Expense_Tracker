package com.example.expenseTracker.mapper;

import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import com.example.expenseTracker.dto.user.UserRequestDTO;
import com.example.expenseTracker.dto.user.UserResponseDTO;
import com.example.expenseTracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ExpenseMapper expenseMapper;


    public User toEntity(UserRequestDTO request){
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public UserResponseDTO toResponse(User user){

        List<ExpenseResponseDTO> expenses = user.getExpenses()
                .stream()
                .map(expense -> new ExpenseResponseDTO(
                        expense.getId(),
                        expense.getTitle(),
                        expense.getAmount(),
                        expense.getExpenseDate(),
                        expense.getUser().getId(),
                        expense.getCategory().getId(),
                        expense.getCategory().getName()
                ))
                .toList();


        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                expenses
        );
    }
}
