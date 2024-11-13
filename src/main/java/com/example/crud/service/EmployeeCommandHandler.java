package com.example.crud.service;

import com.example.crud.commands.CreateEmployeeCommand;
import com.example.crud.commands.DeleteEmployeeCommand;
import com.example.crud.commands.UpdateEmployeeCommand;
import com.example.crud.entity.Employee;
import com.example.crud.event.EmployeeCreatedEvent;
import com.example.crud.event.EmployeeDeletedEvent;
import com.example.crud.event.EmployeeUpdatedEvent;
import com.example.crud.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeCommandHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeProducer employeeProducer;

    public ResponseEntity<Employee> handle(CreateEmployeeCommand command) {
        Employee employee = new Employee(command.getEmployeeName(), command.getEmployeeDescription());
        Employee savedEmployee = employeeRepository.save(employee);
        employeeProducer.sendCreateMessage(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    public ResponseEntity<Employee> handle(UpdateEmployeeCommand command) {
        Optional<Employee> existingEmployee = employeeRepository.findById(command.getEmployeeId());
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setEmployeeName(command.getEmployeeName());
            employee.setEmployeeDescription(command.getEmployeeDescription());
            Employee updatedEmployee = employeeRepository.save(employee);
            employeeProducer.sendUpdateMessage(updatedEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Void> handle(DeleteEmployeeCommand command) {
        if (employeeRepository.existsById(command.getEmployeeId())) {
            employeeRepository.deleteById(command.getEmployeeId());
            employeeProducer.sendDeleteMessage(command.getEmployeeId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
