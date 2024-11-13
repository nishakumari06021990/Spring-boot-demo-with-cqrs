package com.example.crud.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeCommand {
    private Long employeeId;
    private String employeeName;
    private String employeeDescription;
}
