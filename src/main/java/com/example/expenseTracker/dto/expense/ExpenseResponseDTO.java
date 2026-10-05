package com.example.expenseTracker.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private Long userId;
    private Long categoryId;
    private String categoryName;


}
