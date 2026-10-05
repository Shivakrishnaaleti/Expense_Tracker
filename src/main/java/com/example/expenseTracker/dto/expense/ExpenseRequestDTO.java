package com.example.expenseTracker.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ExpenseRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive
    private BigDecimal amount;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Date should not be null")
    private LocalDate expenseDate;

    @NotNull(message = "User should not be null")
    private Long userId;

}
