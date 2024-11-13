package com.example.crud.event;

import com.example.crud.entity.Employee;

public class EmployeeCreatedEvent {
    private final Employee employee;

    public EmployeeCreatedEvent(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
