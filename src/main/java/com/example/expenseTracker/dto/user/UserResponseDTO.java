package com.example.expenseTracker.dto.user;

import com.example.expenseTracker.dto.expense.ExpenseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private List<ExpenseResponseDTO> expenses;

}
