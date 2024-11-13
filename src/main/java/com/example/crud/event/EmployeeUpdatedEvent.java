package com.example.crud.event;

import com.example.crud.entity.Employee;

public class EmployeeUpdatedEvent {
    private final Employee employee;

    public EmployeeUpdatedEvent(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
